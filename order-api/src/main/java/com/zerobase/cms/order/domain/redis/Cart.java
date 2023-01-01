package com.zerobase.cms.order.domain.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@RedisHash("basket") // key의 prefix
public class Cart {
	@Id
	private Long customerId;
	private List<Product> products = new ArrayList<>();
	private List<String> messages = new ArrayList<>();

	public Cart(Long memberId) {
		this.customerId = memberId;
	}

	public void addMessage(String message) {
		messages.add(message);
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Product {
		private Long id;
		private Long sellerId;
		private String name;
		private String description;
		private List<ProductItem> items = new ArrayList<>();

		public static Product from(AddProductCartForm form) {
			return Product.builder()
				.id(form.getId())
				.sellerId(form.getSellerId())
				.name(form.getName())
				.description(form.getDescription())
				.items(form.getItems().stream().map(ProductItem::from).collect(Collectors.toList()))
				.build();
		}
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ProductItem {
		private Long id;
		private String name;
		private Integer price;
		private Integer count;

		public static ProductItem from(AddProductCartForm.ProductItem form) {
			return ProductItem.builder()
				.id(form.getId())
				.name(form.getName())
				.price(form.getPrice())
				.count(form.getCount())
				.build();
		}
	}
}
