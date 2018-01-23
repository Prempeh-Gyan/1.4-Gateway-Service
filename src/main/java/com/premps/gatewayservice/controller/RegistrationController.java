package com.premps.gatewayservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.premps.gatewayservice.model.User;

import lombok.extern.slf4j.Slf4j;

/**
 * This Controller handles service calls from users who want to signup. The call
 * comes through the Gateway-Service at the "/new-user-registration" endpoint.
 * The Gateway-Service then has to propagate this call to the targeted service
 * which in this case is the Signup-Service. But with this implementation, the
 * Gateway-Service does not directly call the Signup-Service, instead, it
 * publishes a message with the user's details to Kafka, this message is then
 * consumed by the Signup-Service which then act on it by registering the user.
 * 
 * @author Prempeh Gyan
 * @version 1.0
 * @since 23/01/2018
 *
 */
@Slf4j
@RestController
public class RegistrationController {

	@Autowired
	private Source source;

	/**
	 * 
	 * @param user
	 *            The message published by the Gateway-Service is a user object
	 *            created from the details provided by the end user.
	 */
	@RequestMapping(value = { "/new-user-registration" }, method = RequestMethod.POST)
	public void setNewUser(@RequestBody User user) {
		log.info("sending message to Kafka for registration service to create this user: {}", user.getFirstName());
		Message<User> message = MessageBuilder.withPayload(user).build();
		this.source.output().send(message);
	}
}
