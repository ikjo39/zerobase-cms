package com.zerobase.cms.order.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.model.ProductItem;
import com.zerobase.cms.order.domain.product.AddProductForm;
import com.zerobase.cms.order.domain.product.UpdateProductForm;
import com.zerobase.cms.order.domain.product.UpdateProductItemForm;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import com.zerobase.cms.order.exception.CustomException;
import com.zerobase.cms.order.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	@Transactional
	public Product addProduct(Long sellerId, AddProductForm form) {
		return productRepository.save(Product.of(sellerId, form));
	}

	@Transactional
	public Product updateProduct(Long sellerId, UpdateProductForm form) {
		Product product = productRepository.findBySellerIdAndId(sellerId, form.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
		product.setName(form.getName());
		product.setDescription(form.getDescription());
		for (UpdateProductItemForm itemForm : form.getItems()) {
			ProductItem item = product.getProductItems().stream()
				.filter(p1 -> p1.getId().equals(itemForm.getId()))
				.findFirst().orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT_ITEM));
			item.setName(itemForm.getName());
			item.setPrice(itemForm.getPrice());
			item.setCount(itemForm.getCount());
		}
		return product;
	}

	@Transactional
	public String deleteProduct(Long sellerId, Long productId) {
		Product product = productRepository.findBySellerIdAndId(sellerId, productId)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
		productRepository.delete(product);
		return product.getName();
	}

	public Product getByProductId(Long productId) {
		return productRepository.findById(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
	}
}