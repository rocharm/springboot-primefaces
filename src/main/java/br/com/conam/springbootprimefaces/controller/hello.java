package br.com.conam.springbootprimefaces.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class hello {
	
	@RequestMapping("/")
	@ResponseBody
	String hello() {
		return "Olá mundo!";
	}

}
