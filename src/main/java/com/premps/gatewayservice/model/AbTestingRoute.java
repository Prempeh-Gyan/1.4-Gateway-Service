package com.premps.gatewayservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbTestingRoute {

	private String serviceName;
	private String active;
	private String endpoint;
	private Integer weight;
}
