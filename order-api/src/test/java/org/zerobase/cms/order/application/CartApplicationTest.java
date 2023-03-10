package org.zerobase.cms.order.application;//package com.ikjo39.commerce.order.application;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerobase.cms.order.domain.model.Product;
import org.zerobase.cms.order.domain.product.AddProductForm;
import org.zerobase.cms.order.domain.redis.AddProductCartForm;
import org.zerobase.cms.order.domain.redis.Cart;
import org.zerobase.cms.order.service.ProductService;

import org.zerobase.cms.order.domain.product.AddProductItemForm;
import org.zerobase.cms.order.domain.repository.ProductRepository;

@SpringBootTest
class CartApplicationTest {
	@Autowired
	private CartApplication cartApplication;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@Test
	void ADD_TEST_MODIFY() {
		Long customerId = 100L;
		cartApplication.clearCart(customerId);
		Product p = add_product();
		Product result = productRepository.findWithProductItemsById(p.getId()).get();
		assertNotNull(result);
		assertEquals(result.getName(), "나이키 에어포스");
		assertEquals(result.getDescription(), "신발");
		assertEquals(result.getProductItems().size(), 3);
		assertEquals(result.getProductItems().get(0).getName(), "나이키 에어포스0");
		assertEquals(result.getProductItems().get(0).getPrice(), 10000);
		// assertEquals(result.getProductItems().get(0).getCount(), 1);
		Cart cart = cartApplication.addCart(customerId, makeAddForm(result));
		assertEquals(cart.getMessages().size(), 0);
		cart = cartApplication.getCart(customerId);
		assertEquals(cart.getMessages().size(), 1);
		//TODO 데이터가 잘 들어갔는지, 각 필드들 점검
	}

	AddProductCartForm makeAddForm(Product p) {
		AddProductCartForm.ProductItem productItem = AddProductCartForm.ProductItem.builder()
			.id(p.getProductItems().get(0).getId())
			.name(p.getProductItems().get(0).getName())
			.count(5)
			.price(20000)
			.build();
		return AddProductCartForm.builder()
			.id(p.getId())
			.sellerId(p.getSellerId())
			.name(p.getName())
			.description(p.getDescription())
			.items(List.of(productItem))
			.build();
	}

	Product add_product() {
		Long sellerId = 1L;
		AddProductForm form = makeProductForm("나이키 에어포스", "신발", 3);
		return productService.addProduct(sellerId, form);
	}

	private static AddProductForm makeProductForm(String name, String description, int count) {
		List<AddProductItemForm> itemForms = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			itemForms.add(
				makeProductItemForm(null, name + i)
			);
		}
		return AddProductForm.builder()
			.name(name)
			.description(description)
			.items(itemForms)
			.build();
	}

	private static AddProductItemForm makeProductItemForm(Long productId, String name) {
		return AddProductItemForm.builder()
			.productId(productId)
			.name(name)
			.price(10000)
			.count(10)
			.build();
	}
}
