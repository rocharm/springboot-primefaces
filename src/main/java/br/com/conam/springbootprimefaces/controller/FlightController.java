package br.com.conam.springbootprimefaces.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
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
import br.com.conam.springbootprimefaces.util.ApplicationException;
import br.com.conam.springbootprimefaces.util.Mensagem;

@ViewAccessScoped
@RestController
@RequestMapping("rest/flight")
public class FlightController extends AbstractController implements Serializable {

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
	 * @throws ApplicationException 
	 */
	public String salvar() {
		try {
			if(flight.getId() == null) {
				flightService.incluir(flight);
			} else {
				flightService.alterar(flight);
			}
			Mensagem.setMessage("descricao.required");
			return LISTAR;
		} catch (ApplicationException e) {
			Mensagem.setMessage(e);
			return null;
		}
		
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
	public ResponseEntity<List<Flight>> obter() throws ApplicationException {
		List<Flight> lista = flightService.findByAirline(null);
        if(Boolean.TRUE) {
        	throw new ApplicationException("descricao.required");
        }
		return new ResponseEntity<List<Flight>>(lista, HttpStatus.OK);
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