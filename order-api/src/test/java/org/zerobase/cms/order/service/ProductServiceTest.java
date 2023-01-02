package org.zerobase.cms.order.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.zerobase.cms.order.domain.product.AddProductForm;
import org.zerobase.cms.order.domain.product.AddProductItemForm;
import org.zerobase.cms.order.domain.model.Product;
import org.zerobase.cms.order.domain.repository.ProductItemRepository;
import org.zerobase.cms.order.domain.repository.ProductRepository;

@SpringBootTest
class ProductServiceTest {

	@Autowired
	private ProductService productService;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductItemRepository productItemRepository;

	@Test
	void ADD_PRODUCT_TEST() {
		Long sellerId = 1L;
		
		AddProductForm form = makeProductForm("나이키 에어포스", "신발", 3);

		Product p =  productService.addProduct(sellerId, form);
		// 하위 객체들이 안보이면 에러가 나옴
		Product result = productRepository.findWithProductItemsById(p.getId()).get();

		assertNotNull(result);
		assertEquals(result.getName(), "나이키 에어포스");
		assertEquals(result.getDescription(), "신발");
		assertEquals(result.getProductItems().size(), 3);
		assertEquals(result.getProductItems().get(0).getName(), "나이키 에어포스0");
		assertEquals(result.getProductItems().get(0).getPrice(), 10000);
		assertEquals(result.getProductItems().get(0).getCount(), 1);
	}

	private static AddProductForm makeProductForm(String name, String description, int itemCount) {
		List<AddProductItemForm> itemForms = new ArrayList<>();
		for (int i = 0; i < itemCount; i++) {
			itemForms.add(makeProductItemForm(null, name+i));
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
			.count(1)
			.build();

	}
}
