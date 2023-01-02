package com.zerobase.cms.user.config.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.zerobase.cms.domain.common.UserVo;
import org.zerobase.cms.domain.config.JwtAuthenticationProvider;

import com.zerobase.cms.user.service.customer.CustomerService;

import lombok.RequiredArgsConstructor;

@WebFilter(urlPatterns = "/customer/*")
@RequiredArgsConstructor
public class CustomFilter implements Filter {

	private final JwtAuthenticationProvider jwtAuthenticationProvider;
	private final CustomerService customerService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		String token = httpServletRequest.getHeader("X-AUTH-TOKEN");
		if (!jwtAuthenticationProvider.validateToken(token)) {
			throw new ServletException("Invalid Access");
		}
		UserVo vo = jwtAuthenticationProvider.getUserVo(token);
		customerService.findByIdAndEmail(vo.getId(), vo.getEmail())
			.orElseThrow(() -> new ServletException("Invalid Access"));
		chain.doFilter(request, response);
	}

}
