package com.geekhub.secretaryhelperapp.config;

import com.geekhub.secretaryhelperapp.user.service.PrincipalUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String ADMIN_ROLE = "ADMIN";

	private static final String USER_ROLE = "USER";

	private static final String LOGIN_URL = "/login";

	private static final String REGISTRATION_URL = "/registration";

	private static final String API_URL_PATTERN = "/api/**";

	private final UserDetailsService userDetailsService;

	@Autowired
	public WebSecurityConfig(final PrincipalUserDetailsService principalUserDetailsService) {
		this.userDetailsService = principalUserDetailsService;
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/", REGISTRATION_URL, LOGIN_URL, "/css/**", "/js/**", "/img/**").permitAll()
				.antMatchers("/calendars", "/events", "/events/show/*").hasAnyRole(USER_ROLE, ADMIN_ROLE)
				.antMatchers("/**/create/**", "/**/edit/**", "/**/update/**", "/**/delete/**").hasRole(ADMIN_ROLE)
				.antMatchers(HttpMethod.POST, API_URL_PATTERN).hasRole(ADMIN_ROLE)
				.antMatchers(HttpMethod.PUT, API_URL_PATTERN).hasRole(ADMIN_ROLE)
				.antMatchers(HttpMethod.DELETE, API_URL_PATTERN).hasRole(ADMIN_ROLE)
				.anyRequest().authenticated()
				.and()
				.formLogin().defaultSuccessUrl("/calendars", true)
				.loginProcessingUrl("/signin").loginPage(LOGIN_URL)
				.usernameParameter("txtUsername").passwordParameter("txtPassword")
				.and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl(LOGIN_URL)
				.and()
				.exceptionHandling().accessDeniedPage("/access-denied");
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		final var authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		authenticationProvider.setUserDetailsService(userDetailsService);
		return authenticationProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
