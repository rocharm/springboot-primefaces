package br.com.conam.springbootprimefaces.util;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortOrder;

import br.com.conam.springbootprimefaces.filter.ProcessorUtils;
import br.com.conam.springbootprimefaces.repository.BaseEntity;

public abstract class Filtro<T extends BaseEntity> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5595589197340659042L;
	
	private Class<T> entityType;

	private String sortField;

	private SortOrder sortOrder;

	private Map<String, FilterMeta> filters;
	
	/**
     * Construtor da classe
     */
	@SuppressWarnings("unchecked")
	protected Filtro() {
    	this.entityType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

	public abstract Predicate makePredicate(final CriteriaBuilder builder, final Root<T> root, final CriteriaQuery<?> query, JoinStrategy joinStrategy);
	
	
	public List<String> getFetches() {
		return Collections.emptyList();
	}
	
	public List<Order> makeOrderBy(CriteriaBuilder builder, Root<T> root, CriteriaQuery<?> query, JoinStrategy joinStrategy) {
		List<Order> orderList = new ArrayList<>();
		if(sortField == null) {
			orderList.addAll(makeDefaultOrderBy(builder, root, query, joinStrategy));
		} else {
			Path<?> orderBy = ProcessorUtils.navigate(root, sortField, joinStrategy);
			switch (sortOrder) {
			case ASCENDING:
				orderList.add(builder.asc(orderBy));
				break;
			case DESCENDING:
				orderList.add(builder.asc(orderBy));
				break;
			default:
				throw new IllegalArgumentException("Tipo de ordenação '" + sortOrder + "' desconhecida.");
			}
		}
		orderList.add(builder.asc(root.get("id")));
		return orderList;
	}

	protected List<Order> makeDefaultOrderBy(CriteriaBuilder builder, Root<T> root, CriteriaQuery<?> query, JoinStrategy joinStrategy) {
		return Arrays.asList(builder.asc(root.get("id")));
	}

	public void setSortInfo(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	public void setFilters(Map<String, FilterMeta> filters) {
		this.filters = filters;
	}
	
	public Map<String, FilterMeta> getFilters() {
		return filters;
	}

	public Class<T> getEntityType() {
		return entityType;
	}


	protected String getSortField() {
		return sortField;
	}


	protected SortOrder getSortOrder() {
		return sortOrder;
	}
}

