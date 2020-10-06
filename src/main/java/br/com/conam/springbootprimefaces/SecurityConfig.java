package br.com.conam.springbootprimefaces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.conam.springbootprimefaces.service.AutenticacaoService;

/**
 * Spring Security Configuration.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AutenticacaoService autenticacaoService;
	
	//Configuracoes de autenticacao
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
	}

	//Configuracoes de recursos estaticos(js, css, imagens, etc.)
	@Override
	public void configure(WebSecurity web) throws Exception {
	}
	
	@Override
	protected void configure(HttpSecurity http) {
		try {
			http.csrf().disable();
//			http
//				.userDetailsService(userDetailsService())
//				.authorizeRequests()
//				.antMatchers("/").permitAll()
//				.antMatchers("/**.jsf").permitAll()
//				.antMatchers("/javax.faces.resource/**").permitAll()
//				.anyRequest().authenticated()
//				.and()
//				.formLogin()
//				.loginPage("/public/login.xhtml")
//				.permitAll()
//				.failureUrl("/public/login.xhtml?error=true")
//				.defaultSuccessUrl("/pages/dashboard/dashboard.xhtml")
//				.and()
//				.logout()
//				.logoutSuccessUrl("/public/login.xhtml")
//				.deleteCookies("JSESSIONID");
			http
			.authorizeRequests()
			.antMatchers("/public/**").permitAll()
			.antMatchers("/static/**").permitAll()
			.antMatchers("/javax.faces.resource/**").permitAll()
			.anyRequest().authenticated()
			.and().formLogin()
			.loginPage("/public/login.xhtml").permitAll()
			.failureUrl("/public/login.xhtml?error=true")
			.defaultSuccessUrl("/pages/dashboard/dashboard.xhtml")
			.and()
			.logout()
			.logoutSuccessUrl("/public/login.xhtml")
			.deleteCookies("JSESSIONID");
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}


}
