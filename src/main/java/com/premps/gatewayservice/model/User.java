package com.premps.gatewayservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

	private long id;

	private String email;

	private String firstName;

	private String lastName;

	private String password;

	private Boolean isEnabled = false;

	private Boolean isAccountNonExpired = false;

	private Boolean isAccountNonLocked = false;

	private Boolean isCredentialsNonExpired = false;

	private Role role;

	public User(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.isEnabled = user.getIsEnabled();
		this.isAccountNonExpired = user.getIsAccountNonExpired();
		this.isAccountNonLocked = user.getIsAccountNonLocked();
		this.isCredentialsNonExpired = user.getIsCredentialsNonExpired();
		this.role = user.getRole();
	}
}
