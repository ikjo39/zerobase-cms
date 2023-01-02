package org.zerobase.cms.user.application;

import org.springframework.stereotype.Service;
import org.zerobase.cms.domain.common.UserType;
import org.zerobase.cms.domain.config.JwtAuthenticationProvider;

import org.zerobase.cms.user.domain.SignInForm;
import org.zerobase.cms.user.domain.model.Customer;
import org.zerobase.cms.user.domain.model.Seller;
import org.zerobase.cms.user.exception.CustomException;
import org.zerobase.cms.user.exception.ErrorCode;
import org.zerobase.cms.user.service.customer.CustomerService;
import org.zerobase.cms.user.service.seller.SellerService;

import lombok.RequiredArgsConstructor;

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
