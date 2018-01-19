package com.premps.gatewayservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.premps.gatewayservice.model.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class RegistrationController {

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private Source source;

	@RequestMapping("/service-instances/{applicationName}")
	public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
		return this.discoveryClient.getInstances(applicationName);
	}

	@RequestMapping(value = { "/new-user-registration" }, method = RequestMethod.POST)
	public void setNewUser(@RequestBody User user) {
		log.info("sending message to Kafka for registration service to create this user: {}", user.getFirstName());
		Message<User> message = MessageBuilder.withPayload(user).build();
		this.source.output().send(message);
	}
}
