package br.com.conam.springbootprimefaces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
public class SpringbootPrimefacesApplication {

//	@RequestMapping("/")
//	@ResponseBody
//	String hello() {
//		return "Olá mundo";
//	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringbootPrimefacesApplication.class, args);
	}

}
