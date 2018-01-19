package com.premps.gatewayservice.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
	private long id;

	private String name;

	private List<CustomGrantedAuthority> privileges;

}
