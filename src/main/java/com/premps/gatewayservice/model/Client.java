package com.premps.gatewayservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client {

	private long id;

	private String clientId;

	private Boolean isSecretRequired;

	private Boolean isScoped;

	private Integer accessTokenValiditySeconds;

	private Integer refreshTokenValiditySeconds;

	private String compressedResourceIds;

	private String clientSecret;

	private String compressedScopes;

	private String compressedAuthorizedGrantTypes;

	private String compressedRegisteredRedirectUris;

	public Client(Client client) {
		this.id = client.getId();
	}

	public boolean isAutoApprove(String scope) {
		return false;
	}

	private Role role;
}
