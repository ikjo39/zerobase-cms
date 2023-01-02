package org.zerobase.cms.user.controller;

import org.zerobase.cms.user.application.SignUpApplication;
import org.zerobase.cms.user.domain.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup") // 로그인 처리하면서 말해줌
@RequiredArgsConstructor
public class SignUpController {

	private final SignUpApplication signUpApplication;

	@PostMapping("/customer")
	public ResponseEntity<String> customerSignUp(@RequestBody SignUpForm form) {
		return ResponseEntity.ok(signUpApplication.customerSignUp(form));
	}

	@GetMapping("/customer/verify")
	public ResponseEntity<String> verifyCustomer(String email, String code) {
		signUpApplication.customerVerify(email, code);
		return ResponseEntity.ok("인증이 완료되었습니다.");
	}

	@PostMapping("/seller")
	public ResponseEntity<String> sellerSignUp(@RequestBody SignUpForm form) {
		return ResponseEntity.ok(signUpApplication.sellerSignUp(form));
	}

	@GetMapping("/seller/verify")
	public ResponseEntity<String> verifySeller(String email, String code) {
		signUpApplication.sellerVerify(email, code);
		return ResponseEntity.ok("인증이 완료되었습니다.");
	}

}