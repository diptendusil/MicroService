package com.cognizant.truyum.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AppDetailsService appDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(appDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.cors();
		httpSecurity.csrf().disable().httpBasic()
			.and()
			.authorizeRequests()
			.antMatchers("/authenticate").hasAnyRole("USER", "ADMIN")
			.antMatchers("/users").anonymous()
			.antMatchers("/users/**").anonymous()
			.antMatchers(HttpMethod.GET, "/menu-items").permitAll()
			.antMatchers(HttpMethod.PUT, "/menu-items").hasRole("ADMIN")
			.antMatchers("/cart/**").hasRole("USER")
			.antMatchers(HttpMethod.GET, "/logout").permitAll()
			.anyRequest().authenticated()
			.and()
			.addFilter(new JwtAuthorizationFilter(authenticationManager()))
			.logout().logoutUrl("/logout");
	}
	
	
	/*
	@Bean
	public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
		List<UserDetails> userDetailsList = new ArrayList<UserDetails>();
		
		userDetailsList.add(User.withUsername("user").password(passwordEncoder().encode("pass1234")).roles("USER").build());
		userDetailsList.add(User.withUsername("admin").password(passwordEncoder().encode("pass1234")).roles("ADMIN").build());
		
		
		return new InMemoryUserDetailsManager(userDetailsList);
	}
	*/
}
