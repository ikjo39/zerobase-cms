package org.zerobase.cms.user.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.zerobase.cms.user.domain.model.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {
	Optional<Seller> findByIdAndEmail(Long id, String email);

	Optional<Seller> findByEmailAndPasswordAndVerifyIsTrue(String email, String password);

	Optional<Seller> findByEmail(String email);
}
