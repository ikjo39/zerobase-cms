package com.zerobase.cms.user.application;

import com.zerobase.cms.domain.common.UserType;
import com.zerobase.cms.domain.config.JwtAuthenticationProvider;
import com.zerobase.cms.user.domain.SignInForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.model.Seller;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import com.zerobase.cms.user.service.customer.CustomerService;
import com.zerobase.cms.user.service.seller.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInApplication {

	private final CustomerService customerService;
	private final SellerService sellerService;
	private final JwtAuthenticationProvider provider;

	public String customerLoginToken(SignInForm form) {
		Customer customer = customerService.findValidCustomer(form.getEmail(), form.getPassword())
			.orElseThrow(() -> new CustomException(ErrorCode.EXPIRED_CODE));
		return provider.createToken(customer.getEmail(), customer.getId(), UserType.CUSTOMER);
	}

	public String sellerLoginToken(SignInForm form) {
		Seller seller = sellerService.findValidSeller(form.getEmail(), form.getPassword())
			.orElseThrow(() -> new CustomException(ErrorCode.EXPIRED_CODE));
		return provider.createToken(seller.getEmail(), seller.getId(), UserType.SELLER);
	}
}
