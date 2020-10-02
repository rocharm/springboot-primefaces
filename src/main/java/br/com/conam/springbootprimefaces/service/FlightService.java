package br.com.conam.springbootprimefaces.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.conam.springbootprimefaces.model.Flight;
import br.com.conam.springbootprimefaces.repository.FlightRepository;

@Service
public class FlightService {

	@Autowired
    private FlightRepository flightRepository;
	
	/**
	 * 
	 * @param flight
	 */
	public void incluir(Flight flight) {
		flightRepository.save(flight);
	}
	
	/**
	 * 
	 * @param flight
	 */
	public void alterar(Flight flight) {
		flightRepository.save(flight);
	}
	
	/**
	 * 
	 * @param flight
	 */
	public void excluir(Flight flight) {
		flightRepository.delete(flight);
	}
	
	/**
	 * 
	 * @param airline
	 * @return
	 */
	public List<Flight> findByAirline(String airline){
        return flightRepository.findAll(new Specification<Flight>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -7012652555322883241L;

			@Override
            public Predicate toPredicate(Root<Flight> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<>();
                
                // ano
                if(airline != null) {                	
                	Predicate p1 = builder.equal(root.get("airline").as(String.class), "a");
                	Predicate p2 = builder.equal(root.get("airline").as(String.class), "b");
                	predicates.add(builder.or(p1, p2));
                }
                
                return builder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
    }
}
