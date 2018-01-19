package com.premps.gatewayservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class Oauth2ResourceConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private ServiceConfig serviceConfig;

	@Bean
	public TokenStore getTokenStore() {
		return new JwtTokenStore(getJwtAccessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter getJwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey(serviceConfig.getJwtSigningKey());
		return converter;
	}

	@Bean
	@Primary
	public DefaultTokenServices defaultTokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(getTokenStore());
		defaultTokenServices.setSupportRefreshToken(true);
		return defaultTokenServices;
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer config) {
		config.tokenServices(defaultTokenServices());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {

// @formatter:off
		http.authorizeRequests()
				.antMatchers("/get-all-users", "/get-user-by-id/**", "/users","/user/**","/test")
				.authenticated()
				.and()
			.authorizeRequests()
				.anyRequest()
				.anonymous();
// @formatter:on

	}
}
