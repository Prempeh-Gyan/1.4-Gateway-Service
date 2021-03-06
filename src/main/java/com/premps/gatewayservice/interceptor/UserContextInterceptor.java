package com.premps.gatewayservice.interceptor;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class UserContextInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body,
			ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {

		HttpHeaders httpHeaders = httpRequest.getHeaders();

		httpHeaders.add(UserContext.CORRELATION_ID, UserContextHolder.getUserContext().getCorrelationId());

		httpHeaders.add(UserContext.AUTH_TOKEN, UserContextHolder.getUserContext().getAuthToken());

		return clientHttpRequestExecution.execute(httpRequest, body);
	}

}
