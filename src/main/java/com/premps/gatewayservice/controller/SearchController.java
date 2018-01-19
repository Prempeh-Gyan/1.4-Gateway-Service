package com.premps.gatewayservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.premps.gatewayservice.feignClient.SearchFeignClient;
import com.premps.gatewayservice.interceptor.UserContext;
import com.premps.gatewayservice.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class SearchController {

	private final @NotNull RestTemplate restTemplate;

	private final @NotNull SearchFeignClient searchFeignClient;

	// @formatter:off
	@HystrixCommand(fallbackMethod = "getAllUsersFallBackMethod",
					commandProperties = {
						@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "6000"),
						@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
						@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
						@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
						@HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "15000"),
						@HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5") 
										}, 
					threadPoolKey = "getAllUsersThreadPool", 
					threadPoolProperties = {
						@HystrixProperty(name = "coreSize", value = "30"),
						@HystrixProperty(name = "maxQueueSize", value = "10") }
					)
	@RequestMapping(value = { "/get-all-users" }, method = RequestMethod.GET)
	public List<User> getAllUsers() {
		
		log.info("Inside Hystrix bulkHead: Authorization = {}", UserContext.getAuthToken());

		log.info("Inside Hystrix bulkHead: CorrelationId = {}", UserContext.getCorrelationId());
		
		log.info("Filter Utils correlationId = {}");
		
		return searchFeignClient.getAllUsers();
	}
	// @formatter:on
	@HystrixCommand(fallbackMethod = "getUserByIdFallBackMethod", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "6000") })
	@RequestMapping(value = { "/get-user-by-id/{id}" }, method = RequestMethod.GET)
	public User getUserById(@PathVariable(name = "id") long id) {
		return searchFeignClient.getUserById(id);
	}

	public List<User> getAllUsersFallBackMethod() {
		List<User> fallBackUsers = new ArrayList<>();
		User user = new User();
		user.setFirstName("Prempeh");
		user.setLastName("Gyan");
		user.setEmail("dummyemail@abc.com");
		fallBackUsers.add(user);

		user = new User();
		user.setFirstName("Peter");
		user.setLastName("Johnson");
		user.setEmail("dummypeter@abc.com");
		fallBackUsers.add(user);

		return fallBackUsers;
	}

	public User getUserByIdFallBackMethod(long id) {
		User fallBackUser = new User();
		fallBackUser.setId(1);
		fallBackUser.setFirstName("PrempehPrempehPrempeh");
		fallBackUser.setLastName("GyanPrempehPrempeh");
		fallBackUser.setEmail("dummyemailPrempeh@abc.com");
		return fallBackUser;
	}

	@RequestMapping(value = { "/users" }, method = RequestMethod.GET)
	public List<User> getUsers() {

		log.info("Outside Hystrix bulkHead: Authorization = {}", UserContext.getAuthToken());

		log.info("Outside Hystrix bulkHead: CorrelationId = {}", UserContext.getCorrelationId());

		HttpHeaders headers = new HttpHeaders();

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ParameterizedTypeReference<List<User>> ptr = new ParameterizedTypeReference<List<User>>() {
		};

		ResponseEntity<List<User>> response = this.restTemplate.exchange("http://SEARCH-SERVICE/all-users",
				HttpMethod.GET, entity, ptr);

		return response.getBody().stream().collect(Collectors.toList());
	}

	@RequestMapping(value = { "/user/{id}" }, method = RequestMethod.GET)
	public ResponseEntity<User> getUsers(@PathVariable("id") long id) {

		log.info("Rest Template: auth token is : {}", UserContext.getAuthToken());

		HttpHeaders headers = new HttpHeaders();

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<User> response = this.restTemplate.exchange("http://SEARCH-SERVICE/user/" + id, HttpMethod.GET,
				entity, User.class);

		return response;
	}

	@RequestMapping(value = { "/test" }, method = RequestMethod.GET)
	public String getSomethingForTesting() {

		log.info("This is the test endpoint");

		return "success!";
	}

}
