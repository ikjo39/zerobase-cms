package org.zerobase.cms.user.service.seller;

import static org.zerobase.cms.user.exception.ErrorCode.ALREADY_VERIFIED;
import static org.zerobase.cms.user.exception.ErrorCode.EXPIRED_CODE;
import static org.zerobase.cms.user.exception.ErrorCode.NOT_FOUND_USER;
import static org.zerobase.cms.user.exception.ErrorCode.WRONG_VERIFICATION;

import org.zerobase.cms.user.domain.SignUpForm;
import org.zerobase.cms.user.domain.model.Seller;
import org.zerobase.cms.user.domain.repository.SellerRepository;
import org.zerobase.cms.user.exception.CustomException;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerService {

	private final SellerRepository sellerRepository;

	public Optional<Seller> findByIdAndEmail(Long id, String email) {
		return sellerRepository.findByIdAndEmail(id, email);
	}

	public Optional<Seller> findValidSeller(String email, String password) {
		return sellerRepository.findByEmailAndPasswordAndVerifyIsTrue(email, password);
	}

	public Seller signUp(SignUpForm form) {
		return sellerRepository.save(Seller.from(form));
	}

	public boolean isEmailExist(String email) {
		return sellerRepository.findByEmail(email).isPresent();
	}

	@Transactional
	public void verifyEmail(String email, String code) {
		Seller seller = sellerRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));
		if (seller.isVerify()) {
			throw new CustomException(ALREADY_VERIFIED);
		} else if (!seller.getVerificationCode().equals(code)) {
			throw new CustomException(WRONG_VERIFICATION);
		} else if (seller.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
			throw new CustomException(EXPIRED_CODE);
		}
		seller.setVerify(true);
	}

	@Transactional
	public LocalDateTime changeSellerValidateEmail(Long sellerId, String verificationCode) {
		Optional<Seller> optionalSeller = sellerRepository.findById(sellerId);

		if (optionalSeller.isPresent()) {
			Seller seller = optionalSeller.get();
			seller.setVerificationCode(verificationCode);
			seller.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
			return seller.getVerifyExpiredAt();
		}
		throw new CustomException(NOT_FOUND_USER);
	}
}
