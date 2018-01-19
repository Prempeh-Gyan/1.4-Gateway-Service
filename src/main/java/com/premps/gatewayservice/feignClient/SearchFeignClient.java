package com.premps.gatewayservice.feignClient;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.premps.gatewayservice.model.User;

@FeignClient("search-service")
public interface SearchFeignClient {

	@RequestMapping(value = { "/all-users" }, method = RequestMethod.GET)
	public List<User> getAllUsers();

	@RequestMapping(value = { "/user/{id}" }, method = RequestMethod.GET)
	public User getUserById(@PathVariable(name = "id") long id);
}
