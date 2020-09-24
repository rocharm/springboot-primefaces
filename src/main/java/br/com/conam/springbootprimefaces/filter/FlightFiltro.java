package br.com.conam.springbootprimefaces.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import br.com.conam.springbootprimefaces.model.Flight;
import br.com.conam.springbootprimefaces.util.Filtro;
import br.com.conam.springbootprimefaces.util.JoinStrategy;

@Component
public class FlightFiltro extends Filtro<Flight> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1438061844128819123L;
	
	private String airline;
	
	@Override
	public Predicate makePredicate(CriteriaBuilder builder, Root<Flight> root, CriteriaQuery<?> query, JoinStrategy joinStrategy) {
		
		List<Predicate> condicoes = new ArrayList<>();
		
		// airline
		if (StringUtils.isNotBlank(airline)) {
			condicoes.add(builder.equal(root.get("airline").as(String.class), airline));
		}
		
		return builder.and(condicoes.stream().toArray(Predicate[]::new));
	}
	
	@Override
	public List<Order> makeDefaultOrderBy(CriteriaBuilder builder, Root<Flight> root, CriteriaQuery<?> query, JoinStrategy joinStrategy) {
		return Arrays.asList(builder.desc(root.get("airline")));
	}

	public String getAirline() {
		return airline;
	}

	public void setAirline(String airline) {
		this.airline = airline;
	}
}
