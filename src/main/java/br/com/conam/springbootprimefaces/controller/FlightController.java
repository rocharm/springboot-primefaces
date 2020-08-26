package br.com.conam.springbootprimefaces.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.conam.springbootprimefaces.model.Flight;
import br.com.conam.springbootprimefaces.repository.FlightRepository;

@Named
@ViewScoped
public class FlightController implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Flight flight = new Flight();
	private Flight filtro = new Flight();

    private List<Flight> flights = new ArrayList<>();

    @Autowired
    private FlightRepository flightRepository;

    public void fetchAll() {
        flights = flightRepository.findAll();
    }

    public void save() {
        flightRepository.save(flight);
        flight = new Flight();
        flights = flightRepository.findAll();
    }

    public void edit(Flight flight) {
        this.flight = flight;
    }

    public void refresh() {
        flight = new Flight();
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

	public Flight getFiltro() {
		return filtro;
	}

	public void setFiltro(Flight filtro) {
		this.filtro = filtro;
	}

}