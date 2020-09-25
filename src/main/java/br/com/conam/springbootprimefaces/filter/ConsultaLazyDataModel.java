package br.com.conam.springbootprimefaces.filter;
 
import static br.com.conam.springbootprimefaces.util.JoinStrategy.LEFT_JOIN_FETCH;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Field;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.com.conam.springbootprimefaces.repository.BaseEntity;
import br.com.conam.springbootprimefaces.util.Fetcher;
import br.com.conam.springbootprimefaces.util.Filtro;
import br.com.conam.springbootprimefaces.util.JoinStrategy;

@Service
public class ConsultaLazyDataModel<T extends BaseEntity> extends LazyDataModel<T> implements SelectableDataModel<T> {
	
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 8682213296114288337L;
	
	private Boolean distinct = true;


	@Autowired
    private EntityManager em;

	private Filtro<T> filtro;
	
	private List<T> itens;
	
    @Override
    public Object getRowKey(T entity) {
        return entity.getId();
    }
	
    @Override
    public T getRowData(String rowKey) {
    	Long longRowKey = Long.parseLong(rowKey);
        for(T item : itens) {
            if(item.getId().equals(longRowKey))
                return item;
        }
        return null;
    }
    
    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, FilterMeta> filters) {

    	filtro.setSortInfo(sortField, sortOrder);
    	filtro.setFilters(filters);
    	
    	itens = this.buscar(first, pageSize);
        setPageSize(pageSize);
        
        int quantidade = this.contar();
		setRowCount(quantidade);

        return itens;
    }
    
    
	private List<T> buscar(final int start, final int max) {
        List<Object[]> tuples = filter();
        List<Long> ids = resolveIds(tuples, start, max);
        List<T> result = fetch(ids);
        return result;
    }
    
    /**
     * Executa consulta de registros para apresentação, com fetch dos atributos desejados.
     * @param filtro Filtro utilizado para consulta.
     * @param ids Coleção de ids dos registros desejados.
     * @return Coleção de registros obtidos a partir do filtro de consulta.
     */
    private List<T> fetch(final List<Long> ids) {
        List<T> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(ids)) {
            CriteriaQuery<T> cq = getFetchQuery(ids);
            result = em.createQuery(cq).getResultList();
        }
        return result;
    }

    private CriteriaQuery<T> getFetchQuery(final List<Long> ids) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(filtro.getEntityType());
        Root<T> root = cq.from(filtro.getEntityType());

        fetchFields(root);

        cq.select(root).distinct(distinct);
        cq.where(root.get("id").in(ids));
        cq.orderBy(filtro.makeOrderBy(cb, root, cq, LEFT_JOIN_FETCH));

        return cq;
    }

    /**
     * Executa consulta para filtragem de registros.
     * @param filtro Filtro utilizado para consulta.
     * @return Coleção de registros obtidos a partir do filtro de consulta.
     */
    private List<Object[]> filter() {
        CriteriaQuery<Object[]> cq = getFilterQuery();
        TypedQuery<Object[]> typedQuery = em.createQuery(cq);
        return typedQuery.getResultList();
    }
    
    // FIXME Melhorar a peformance deste método.
    private List<Long> resolveIds(final List<Object[]> tuples, final int start, final int max) {
        List<Long> ids = tuples.stream()
        	.map((Object[] tuple) -> (Long) tuple[0])
        	.collect(toList()); 
        LinkedHashSet<Long> idsSemRepeticao = new LinkedHashSet<>(ids);
        if (start < idsSemRepeticao.size()) {
            int end = Math.min(start + max, idsSemRepeticao.size());
            return new ArrayList<>(idsSemRepeticao).subList(start, end);
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Cria {@code CriteriaQuery} para filtragem a partir de um filtro. Não realiza fetch dos campos.
     * @param <T> Tipo da entidade buscada por este repositório
     * @param filtro Filtro de busca utilizado.
     * @return {@code CriteriaQuery} criado a partir do filtro.
     */
    private CriteriaQuery<Object[]> getFilterQuery() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<T> root = cq.from(filtro.getEntityType());

        JoinStrategy joinStrategy = JoinStrategy.LEFT_JOIN;
        
        List<Order> order = filtro.makeOrderBy(cb, root, cq, joinStrategy);
        Expression<?>[] columns = getColumns(root, order);
        
        cq.multiselect(columns);
        
		Predicate predicate = makeFinalPredicate(cb, root, cq, joinStrategy);
		cq.where(predicate);
		
        cq.orderBy(order);
        
        return cq;
    }

    private Expression<?>[] getColumns(final Root<T> root, final List<Order> order) {
        Expression<?>[] columns = new Expression<?>[order.size() + 1];
        columns[0] = root.get("id");
        for (int i = 0; i < order.size(); i++) {
            columns[i + 1] = order.get(i).getExpression();
        }
        return columns;
    }

    private int contar() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<T> root = query.from(filtro.getEntityType());

        query.select(builder.countDistinct(root));
		Predicate predicate = makeFinalPredicate(builder, root, query, JoinStrategy.LEFT_JOIN);
		query.where(predicate);
        return em.createQuery(query).getSingleResult().intValue();
    }

    /**
     * Faz fetch nos campos desejados.
     * @param <T> Tipo da entidade buscada por este repositório
     * @param filtro Fitro de busca.
     * @param root Root para criação da query.
     */
    private void fetchFields(final Root<T> root) {
        Fetcher fetcher = new Fetcher();
        for (String path : filtro.getFetches()) {
        	String[] splitPath = path.split("\\.");
            fetcher.fetch(root, splitPath);
        }
    }
    
    private Predicate makeFinalPredicate(CriteriaBuilder builder, Root<T> root, CriteriaQuery<?> query, JoinStrategy joinStrategy) {
        Predicate predicate = filtro.makePredicate(builder, root, query, joinStrategy);
		Predicate automatedPredicate = makeAutomatedPredicate(builder, root, query, joinStrategy);
//		Predicate qualificadorOrgaoPredicate = makeQualificadorOrgaoPredicate(builder, root, query, joinStrategy);
		return builder.and(predicate, automatedPredicate);
    }
    
	private Predicate makeAutomatedPredicate(CriteriaBuilder builder, Root<T> root, CriteriaQuery<?> query, JoinStrategy joinStrategy) {
		Collection<Predicate> condicoes = new ArrayList<>();
		
		for (Entry<String, FilterMeta> e : filtro.getFilters().entrySet()) {
			String pathString = e.getKey();
			String filterValue = e.getValue().toString();
			
			// Converte a coluna para varchar, lowercase, sem acentos
			Path<?> path = ProcessorUtils.navigate(root, pathString, joinStrategy);
			Expression<String> expr = path.as(String.class);
			expr = builder.function("unaccent", String.class, expr);
			expr = builder.lower(expr);
			
			// Converte o valor do filtro para lower case sem acentos
            filterValue = Normalizer.normalize(filterValue, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            filterValue = StringUtils.lowerCase(filterValue);

			Predicate containsValue = builder.like(expr, "%" + filterValue + "%");
			condicoes.add(containsValue);
		}
		
		return builder.and(condicoes.stream().toArray(Predicate[]::new));
	}
	
	private Predicate makeQualificadorOrgaoPredicate(CriteriaBuilder builder, Root<T> root, CriteriaQuery<?> query, JoinStrategy joinStrategy) {
		Collection<Predicate> condicoes = new ArrayList<>();
		
		// campos diretamente relacionados a entidade
		Field[] fields = root.getJavaType().getDeclaredFields();
		for(Field field : fields) {
			if(field.getAnnotatedType().getType().getTypeName().equals("br.com.conam.mcl.entity.Orgao")) {
//				Orgao orgao = new Orgao();
//				Path<?> path = ProcessorUtils.navigate(root, field.getName(), JoinStrategy.LEFT_JOIN_FETCH);
//				condicoes.add(builder.equal(path.as(Orgao.class), orgao));
			}
		}
		return builder.and(condicoes.stream().toArray(Predicate[]::new));
	}

    public void setFiltro(Filtro<T> filtro) {
    	this.filtro = filtro;
    }

	public Boolean getDistinct() {
		return distinct;
	}

	public void setDistinct(Boolean distinct) {
		this.distinct = distinct;
	}
}