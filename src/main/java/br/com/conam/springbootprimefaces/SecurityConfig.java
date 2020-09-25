package br.com.conam.springbootprimefaces;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Spring Security Configuration.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) {
		try {
			http.csrf().disable();
			http
				.userDetailsService(userDetailsService())
				.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/**.jsf").permitAll()
				.antMatchers("/javax.faces.resource/**").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/public/login.xhtml")
				.permitAll()
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

	@SuppressWarnings("deprecation")
	@Override
	protected UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager result = new InMemoryUserDetailsManager();
		result.createUser(User.withDefaultPasswordEncoder().username("persapiens").password("123").authorities("ROLE_ADMIN").build());
		result.createUser(User.withDefaultPasswordEncoder().username("nyilmaz").password("qwe").authorities("ROLE_USER").build());
		return result;
	}
}
