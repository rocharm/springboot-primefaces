package br.com.conam.springbootprimefaces.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.conam.springbootprimefaces.model.Flight;

public interface FlightRepository extends JpaRepository<Flight, Long> {

}

