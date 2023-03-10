package org.zerobase.cms.user.service.customer;

import static org.zerobase.cms.user.exception.ErrorCode.*;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import org.zerobase.cms.user.domain.SignUpForm;
import org.zerobase.cms.user.domain.model.Customer;
import org.zerobase.cms.user.domain.repository.CustomerRepository;
import org.zerobase.cms.user.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignUpCustomerService {

	private final CustomerRepository customerRepository;

	public Customer signUp(SignUpForm form) {
		return customerRepository.save(Customer.from(form));
	}

	public boolean isEmailExist(String email) {
		return customerRepository.findByEmail(email.toLowerCase(Locale.ROOT))
			.isPresent();
	}

	@Transactional
	public void verifyEmail(String email, String code) {
		Customer customer = customerRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));
		if (customer.isVerify()) {
			throw new CustomException(ALREADY_VERIFIED);
		} else if (!customer.getVerificationCode().equals(code)) {
			throw new CustomException(WRONG_VERIFICATION);
		} else if (customer.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
			throw new CustomException(EXPIRED_CODE);
		}
		customer.setVerify(true);
	}

	// transactional 걸면 save 안해도 바뀐게 있으면 save 해줌
	@Transactional
	public LocalDateTime changeCustomerValidateEmail(Long customerId, String verificationCode) {
		Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

		if (optionalCustomer.isPresent()) {
			Customer customer = optionalCustomer.get();
			customer.setVerificationCode(verificationCode);
			customer.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
			return customer.getVerifyExpiredAt();
		}
		throw new CustomException(NOT_FOUND_USER);
	}
}
