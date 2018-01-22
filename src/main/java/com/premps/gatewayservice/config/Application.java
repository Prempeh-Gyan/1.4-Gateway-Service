package com.premps.gatewayservice.config;

import java.util.Collections;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;

import com.premps.gatewayservice.interceptor.UserContextInterceptor;

/**
 * This class has the configurations to set up the Gateway-Service.
 * 
 * @author Prempeh Gyan
 * @version 1.0
 * @since 22/01/2018
 *
 */
@EnableZuulProxy // Sets up a Zuul server endpoint and installs some reverse proxy filters
@EnableCircuitBreaker
@EnableFeignClients(basePackages = { "com.premps.gatewayservice.feignClient" })
@EnableBinding(Source.class)
@EnableResourceServer
@ComponentScan(basePackages = { "com.premps.gatewayservice" })
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * 
	 * @return When a service call is made through the Gateway-Service, the targeted
	 *         service is invoked by the Gateway using the RestTemplate. In order to
	 *         ensure that data from the headers of the original service call is
	 *         propagated to the targeted service, a custom UserContex is used to
	 *         hold this information and then injected into the RestTemplate through
	 *         the UserContextInterceptor.
	 */
	@Bean
	@LoadBalanced 
	public RestTemplate getRestTemplate() {

		RestTemplate restTemplate = new RestTemplate();

		List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();

		if (interceptors == null) {
			restTemplate.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
		} else {
			interceptors.add(new UserContextInterceptor());
			restTemplate.setInterceptors(interceptors);
		}

		return restTemplate;
	}
}
