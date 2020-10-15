package br.com.conam.springbootprimefaces;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import br.com.conam.springbootprimefaces.util.Mensagem;

@Configuration
public class AppConfig {
	
	@Bean
    public Mensagem mensagem() {
        return new Mensagem();
    }

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource bean = new ReloadableResourceBundleMessageSource();
		bean.setBasename("classpath:messages");
		bean.setDefaultEncoding("ISO-8859-1");
		return bean;
	}
}
