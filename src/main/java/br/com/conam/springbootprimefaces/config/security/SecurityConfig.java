package br.com.conam.springbootprimefaces.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.conam.springbootprimefaces.repository.UsuarioRepository;



/**
 * Spring Security Configuration.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AutenticacaoService autenticacaoService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
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
			http.authorizeRequests()
				.antMatchers("/public/**").permitAll()
				.antMatchers("/static/**").permitAll()
				.antMatchers("/javax.faces.resource/**").permitAll()
				.antMatchers(HttpMethod.POST, "/auth").permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/public/login.xhtml").permitAll()
				.failureUrl("/public/login.xhtml?error=true")
				.defaultSuccessUrl("/pages/dashboard/dashboard.xhtml")
				.and()
			.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
				.and()
			.addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class)
			.logout()
			.logoutSuccessUrl("/public/login.xhtml")
			.deleteCookies("JSESSIONID");

			
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
