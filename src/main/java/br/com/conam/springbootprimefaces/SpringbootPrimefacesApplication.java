package br.com.conam.springbootprimefaces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SpringbootPrimefacesApplication extends SpringBootServletInitializer {
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpringbootPrimefacesApplication.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringbootPrimefacesApplication.class, args);
	}

}

@RestController
class HelloController {

	@GetMapping("/home")
	@ResponseBody
	public String home() {
		return "home";
	}
}