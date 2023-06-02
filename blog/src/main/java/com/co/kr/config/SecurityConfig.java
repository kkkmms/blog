package com.co.kr.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.co.kr.config.auth.PrincipalDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private PrincipalDetailService principalDetailService;

	@Bean
	BCryptPasswordEncoder encodePWD() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
	    return authenticationConfiguration.getAuthenticationManager();
	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
	}

	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.csrf()
		.disable()
		.authorizeRequests()
		.antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**", "/dummy/**")		
		.permitAll()
		.anyRequest()
		.authenticated()
		.and().formLogin()
		.loginPage("/auth/loginForm")
	    .loginProcessingUrl("/auth/loginProc")
	    .defaultSuccessUrl("/");
		return http.build();
	}
}