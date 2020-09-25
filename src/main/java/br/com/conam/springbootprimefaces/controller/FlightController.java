package br.com.conam.springbootprimefaces.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import javax.faces.view.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.conam.springbootprimefaces.filter.ConsultaLazyDataModel;
import br.com.conam.springbootprimefaces.filter.FlightFiltro;
import br.com.conam.springbootprimefaces.model.Flight;
import br.com.conam.springbootprimefaces.service.FlightService;

@RequestScoped
@RestController
@RequestMapping("flight")
public class FlightController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private FlightService flightService;

	@Autowired
	private ConsultaLazyDataModel<Flight> flightList;

	@Autowired
	private FlightFiltro filtro;

	private Flight flight = new Flight();

	private final String EDITAR = "/pages/flight/flight_editar";
	private final String LISTAR = "/pages/flight/flight_listar";
	private final String EXIBIR = "/pages/flight/flight_exibir";

	@PostConstruct
	public void init() {
		flightList.setFiltro(filtro);
	}

	/**
	 * 
	 * @return
	 */
	public String incluir() {
		flight = new Flight();
		return EDITAR;
	}

	/**
	 * 
	 * @param flight
	 * @return
	 */
	public String editar(Flight flight) {
		this.flight = flight;
		return EDITAR;
	}
	
	/**
	 * 
	 * @param flight
	 * @return
	 */
	public String exibir(Flight flight) {
		this.flight = flight;
		return EXIBIR;
	}

	/**
	 * 
	 * @return
	 */
	public String salvar() {
		if(flight.getId() == null) {
			flightService.incluir(flight);
		} else {
			flightService.alterar(flight);
		}
		return LISTAR;
	}

	/**
	 * 
	 * @param flight
	 */
	public void excluir(Flight flight) {
		flightService.excluir(flight);
	}

	/**
	 * 
	 * @return
	 */
	public String voltar() {
		return LISTAR;
	}

	/**
	 * Método para a consulta padrão
	 * 
	 * @return lista Processo
	 */
	public ConsultaLazyDataModel<Flight> listar() {
		return flightList;
	}
	
	@GetMapping
	public ResponseEntity<String> obter() {
		String finalResponse = "teste";
        
		return new ResponseEntity<String>(finalResponse, HttpStatus.OK);
	}

	/******************************************************************************************************
	 * GET e SET
	 ******************************************************************************************************/

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public FlightFiltro getFiltro() {
		return filtro;
	}

	public void setFiltro(FlightFiltro filtro) {
		this.filtro = filtro;
	}

}