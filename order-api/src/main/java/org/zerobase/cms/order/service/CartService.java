package org.zerobase.cms.order.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import org.zerobase.cms.order.client.RedisClient;
import org.zerobase.cms.order.domain.redis.AddProductCartForm;
import org.zerobase.cms.order.domain.redis.Cart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
	private final RedisClient redisClient;

	public Cart getCart(Long customerId) {
		Cart cart = redisClient.get(customerId, Cart.class);
		return cart != null ? cart : new Cart();
	}

	public Cart putCart(Long customerId, Cart cart) {
		redisClient.put(customerId, cart);
		return cart;
	}

	public Cart addCart(Long customerId, AddProductCartForm form) {
		Cart cart = redisClient.get(customerId, Cart.class);
		if (cart == null) {
			cart = new Cart();
			cart.setCustomerId(customerId);
		}
		// 이전에 같은 상품이 있냐
		Optional<Cart.Product> optionalProduct = cart.getProducts().stream()
			.filter(product1 -> product1.getId().equals(form.getId()))
			.findFirst();
		if (optionalProduct.isPresent()) {
			Cart.Product redisProduct = optionalProduct.get();
			List<Cart.ProductItem> items = form.getItems().stream()
				.map(Cart.ProductItem::from).collect(Collectors.toList());
			Map<Long, Cart.ProductItem> redisItemMap = redisProduct.getItems().stream()
				.collect(Collectors.toMap(Cart.ProductItem::getId, item -> item));
			if (!redisProduct.getName().equals(form.getName())) {
				cart.addMessage(redisProduct.getName() + "의 정보가 변경되었습니다. 확인 부탁드립니다.");
			}
			for (Cart.ProductItem item : items) {
				Cart.ProductItem redisItem = redisItemMap.get(item.getId());
				if (redisItem == null) {
					// happy Case
					redisProduct.getItems().add(item);
				} else {

					if (!redisItem.getPrice().equals(item.getPrice())) {
						cart.addMessage(redisProduct.getName() + ", " + item.getName() + " 의 가격이 변동되었습니다. 확인 부탁드립니다.");
					}
					redisItem.setCount(redisItem.getCount() + item.getCount());
				}
			}
		} else {
			Cart.Product product = Cart.Product.from(form);
			cart.getProducts().add(product);
		}
		redisClient.put(customerId, cart);
		return cart;
	}

	private String of(LocalDate date) {
		return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
}
