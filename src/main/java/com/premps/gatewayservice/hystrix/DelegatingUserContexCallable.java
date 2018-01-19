package com.premps.gatewayservice.hystrix;

import java.util.concurrent.Callable;

import com.premps.gatewayservice.interceptor.UserContext;
import com.premps.gatewayservice.interceptor.UserContextHolder;

public class DelegatingUserContexCallable<T> implements Callable<T> {

	private final Callable<T> delegate;
	private UserContext originalUserContext;

	public DelegatingUserContexCallable(Callable<T> delegate, UserContext userContext) {
		this.delegate = delegate;
		this.originalUserContext = userContext;
	}

	@Override
	public T call() throws Exception {

		UserContextHolder.setUserContext(originalUserContext);

		try {
			return delegate.call();
		} finally {
			this.originalUserContext = null;
		}
	}

	public static <T> Callable<T> create(Callable<T> delegate, UserContext userContext) {
		return new DelegatingUserContexCallable<>(delegate, userContext);
	}

}
