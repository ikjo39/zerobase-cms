package com.zerobase.cms.user.application;


import org.zerobase.cms.domain.common.UserType;
import com.zerobase.cms.user.client.MailgunClient;
import com.zerobase.cms.user.client.mailgun.SendMailForm;
import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.model.Seller;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import com.zerobase.cms.user.service.customer.SignUpCustomerService;
import com.zerobase.cms.user.service.seller.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

// 크게 봤을 때 다른 역할을 하는 서비스 일때 분리함
// 1. 회원가입, 2. 메일을 보낸다.
@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpApplication {

	private final MailgunClient mailgunClient;
	private final SignUpCustomerService signUpCustomerService;
	private final SellerService sellerService;

	public String customerSignUp(SignUpForm form) {
		if (signUpCustomerService.isEmailExist(form.getEmail())) {
			throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
		} else {
			Customer customer = signUpCustomerService.signUp(form);
			String code = getRandomCode();
			SendMailForm sendMailForm = SendMailForm.builder()
				.from("test@tester.com")
				.to(form.getEmail())
				.subject("Verification Email!!")
				.text(getVerificationEmailBody(customer.getEmail(), customer.getName(),
					UserType.CUSTOMER.toString().toLowerCase(), code))
				.build();
			log.info("Send email result: " + mailgunClient.sendEmail(sendMailForm).getBody());
			signUpCustomerService.changeCustomerValidateEmail(customer.getId(), code);
			return "회원가입에 성공하였습니다.";
		}
	}

	public void customerVerify(String email, String code) {
		signUpCustomerService.verifyEmail(email, code);
	}

	public String sellerSignUp(SignUpForm form) {
		if (sellerService.isEmailExist(form.getEmail())) {
			throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
		} else {
			Seller seller = sellerService.signUp(form);
			String code = getRandomCode();
			SendMailForm sendMailForm = SendMailForm.builder()
				.from("test@tester.com")
				.to(form.getEmail())
				.subject("Verification Email!!")
				.text(getVerificationEmailBody(seller.getEmail(), seller.getName(),
					UserType.SELLER.toString().toLowerCase(), code))
				.build();
			log.info("Send email result: " + mailgunClient.sendEmail(sendMailForm).getBody());
			sellerService.changeSellerValidateEmail(seller.getId(), code);
			return "회원가입에 성공하였습니다.";
		}
	}

	public void sellerVerify(String email, String code) {
		sellerService.verifyEmail(email, code);
	}

	private String getRandomCode() {
		return RandomStringUtils.random(10, true, true);
	}

	private String getVerificationEmailBody(String email, String name, String type, String code) {
		StringBuilder builder = new StringBuilder();
		return builder.append("Hello ").append(name)
			.append("! Please Click Link for verification\n\n")
			.append(("http://localhost:8081/signup/"+ type +"/verify?email="))
			.append(email)
			.append("&code=")
			.append(code).toString();
	}

}
