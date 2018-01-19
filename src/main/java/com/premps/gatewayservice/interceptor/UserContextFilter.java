package com.premps.gatewayservice.interceptor;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserContextFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

		UserContextHolder.getUserContext().setCorrelationId(httpServletRequest.getHeader(UserContext.CORRELATION_ID));
		UserContextHolder.getUserContext().setUserId(httpServletRequest.getHeader(UserContext.USER_ID));
		UserContextHolder.getUserContext().setAuthToken(httpServletRequest.getHeader(UserContext.AUTH_TOKEN));
		UserContextHolder.getUserContext().setOrgId(httpServletRequest.getHeader(UserContext.ORG_ID));

		filterChain.doFilter(httpServletRequest, servletResponse);

		log.info("CorrelationId = {} ----- UserId = {} ----- OauthToken = {} ----- OrgId = {}",
				UserContextHolder.getUserContext().getCorrelationId(), UserContextHolder.getUserContext().getUserId(),
				UserContextHolder.getUserContext().getAuthToken(), UserContextHolder.getUserContext().getOrgId());

	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
