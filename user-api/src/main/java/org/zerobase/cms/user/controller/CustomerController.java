package org.zerobase.cms.user.controller;

import static org.zerobase.cms.user.exception.ErrorCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerobase.cms.domain.common.UserVo;
import org.zerobase.cms.domain.config.JwtAuthenticationProvider;

import org.zerobase.cms.user.domain.customer.ChangeBalanceForm;
import org.zerobase.cms.user.domain.customer.CustomerDto;
import org.zerobase.cms.user.domain.model.Customer;
import org.zerobase.cms.user.exception.CustomException;
import org.zerobase.cms.user.service.customer.CustomerBalanceService;
import org.zerobase.cms.user.service.customer.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
	private final JwtAuthenticationProvider provider;
	private final CustomerService customerService;
	private final CustomerBalanceService customerBalanceService;

	@GetMapping("/getInfo")
	public ResponseEntity<CustomerDto> getInfo(@RequestHeader(name = "X-AUTH-TOKEN") String token) {
		UserVo vo = provider.getUserVo(token);
		Customer customer = customerService.findByIdAndEmail(vo.getId(), vo.getEmail())
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));
		return ResponseEntity.ok(CustomerDto.from(customer));
	}

	@PostMapping("/balance")
	public ResponseEntity<Integer> changeBalance(@RequestHeader(name = "X-AUTH-TOKEN") String token,
		@RequestBody ChangeBalanceForm form) {
		UserVo vo = provider.getUserVo(token);
		return ResponseEntity.ok(customerBalanceService.changeBalance(vo.getId(), form).getCurrentMoney());
	}
}
