package org.zerobase.cms.order.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.zerobase.cms.order.domain.model.Product;
import org.zerobase.cms.order.exception.ErrorCode;

import org.zerobase.cms.order.domain.repository.ProductRepository;
import org.zerobase.cms.order.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductSearchService {
	private final ProductRepository productRepository;

	public List<Product> searchByName(String name) {
		return productRepository.searchByName(name);
	}

	public Product getByProductId(Long productId) {
		return productRepository.findWithProductItemsById(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
	}

	public List<Product> getListByProductIds(List<Long> productIds) {
		return productRepository.findAllByIdIn(productIds);
	}
}
