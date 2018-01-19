package com.premps.gatewayservice.interceptor;

import org.springframework.util.Assert;

public class UserContextHolder {

	private static final ThreadLocal<UserContext> threadLocalUserContext = new ThreadLocal<UserContext>();

	public static final UserContext getUserContext() {

		UserContext userContext = threadLocalUserContext.get();

		if (userContext == null) {
			userContext = createEmptyUserContext();
			threadLocalUserContext.set(userContext);
		}

		return threadLocalUserContext.get();
	}

	public static final void setUserContext(UserContext userContext) {

		Assert.notNull(userContext, "Only non-null UserContext instances are permitted");

		threadLocalUserContext.set(userContext);
	}

	public static final UserContext createEmptyUserContext() {
		return new UserContext();
	}

}
