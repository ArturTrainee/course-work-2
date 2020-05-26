package com.geekhub.secretaryhelperapp.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	private final MessageSource messageSource;

	public WebMvcConfig(final MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public Validator getValidator() {
		final var factory = new LocalValidatorFactoryBean();
		factory.setValidationMessageSource(messageSource);
		return factory;
	}

	@Bean
	public SpringSecurityDialect securityDialect() {
		return new SpringSecurityDialect();
	}

	@Bean
	public IDialect conditionalCommentDialect() {
		return new Java8TimeDialect();
	}

}
