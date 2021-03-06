package com.example.JavaDummyStack.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private CustomAuthenicationEntryPoint jwtAuthenticationEntryPoint;

	public SecurityConfiguration(CustomAuthenicationEntryPoint jwtAuthenticationEntryPoint) {
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		// this.jwtRequestFilter = jwtRequestFilter;
	}

	@Autowired
	private UserDetailsService jwtUserDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.and().authorizeRequests().antMatchers("/user/login", "/admin/login", "/user").permitAll().and()
				.authorizeRequests().anyRequest().authenticated().and()
				.addFilterBefore(perRequestFilter(), UsernamePasswordAuthenticationFilter.class).sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Bean
	public JWTAuthorizationFilter perRequestFilter() {
		return new JWTAuthorizationFilter();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	private final String[] AUTH_WHITELIST = { "/*.html", "/images/**", "/favicon.ico", "/**.png", "/**/*.html",
			"/**/*.jsp", "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/*.jpg", "/*.png", "/**/*.png",
			"/**/*.jpeg", "/**/*.JPG", "/**/*.pdf", "/**/*.mp4", "/**/*.3gp", "/**/*.wmv", "/**/*.flv", "/**/*.avi",
			"/**/*.mov", "/swagger-ui/index.html", "/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs",
			"/v3/api-docs", "/webjars/**", "/v3/api-docs/swagger-config" };

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(AUTH_WHITELIST);
	}

}
