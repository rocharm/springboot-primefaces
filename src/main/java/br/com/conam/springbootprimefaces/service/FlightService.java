package br.com.conam.springbootprimefaces.service;

import org.springframework.beans.factory.annotation.Autowired;
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
}
