package com.zerobase.cms.user.application;

import com.zerobase.cms.domain.common.UserType;
import com.zerobase.cms.domain.config.JwtAuthenticationProvider;
import com.zerobase.cms.user.domain.SignInForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import com.zerobase.cms.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInApplication {

	private final CustomerService customerService;
	private final JwtAuthenticationProvider provider;

	public String customerLoginToken(SignInForm form) {
		// 1. 로그인 가능 여부
		Customer customer = customerService.findValidCustomer(form.getEmail(), form.getPassword())
			.orElseThrow(() -> new CustomException(ErrorCode.EXPIRED_CODE));
		// 2. 토큰을 발행하고
		// 3. 토큰을 response한다.
		return provider.createToken(customer.getEmail(), customer.getId(), UserType.CUSTOMER);
	}
}
