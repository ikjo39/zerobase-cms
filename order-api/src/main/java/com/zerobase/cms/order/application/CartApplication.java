package com.zerobase.cms.order.application;

import static com.zerobase.cms.order.exception.ErrorCode.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.model.ProductItem;
import com.zerobase.cms.order.domain.redis.AddProductCartForm;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.exception.CustomException;
import com.zerobase.cms.order.service.CartService;
import com.zerobase.cms.order.service.ProductSearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// 강사님은 서비스 구분의 용이를 위해 application을 따로 만듬
@Slf4j
@Service
@RequiredArgsConstructor
public class CartApplication {
	private final ProductSearchService productSearchService;
	private final CartService cartServoce;

	public Cart addCart(Long customerId, AddProductCartForm form) {
		Product product = productSearchService.getByProductId(form.getId());
		if (product == null) {
			throw new CustomException(NOT_FOUND_PRODUCT);
		}
		Cart basket = cartServoce.getCart(customerId);
		if (basket != null && !addAble(basket, product, form)) {
			throw new CustomException(ITEM_COUNT_NOT_ENOUGH);
		}
		return cartServoce.addCart(customerId, form);
	}

	public Cart updateCart(Long memberId, Cart basket) {
		cartServoce.putCart(memberId, basket);
		return getCart(memberId);
	}

	public Cart getCart(Long customerId) {
		Cart cart = refreshCart(cartServoce.getCart(customerId));
		cart = refreshCart(cart);
		cartServoce.putCart(cart.getCustomerId(), cart);
		Cart returnCart = new Cart();
		returnCart.setCustomerId(cart.getCustomerId());
		returnCart.setProducts(cart.getProducts());
		returnCart.setMessages(cart.getMessages());
		cart.setMessages(new ArrayList<>());
		cartServoce.putCart(customerId, cart);
		return returnCart;
	}

	public void clearCart(Long memberId) {
		cartServoce.putCart(memberId, null);
	}

	protected Cart refreshCart(Cart cart) {
		Map<Long, Product> productMap = productSearchService.getListByProductIds(
				cart.getProducts().stream().map(Cart.Product::getId).collect(
					Collectors.toList()))
			.stream()
			.collect(Collectors.toMap(Product::getId, product -> product));
		// TODO: 각각 케이스 에러가 정상 출력되는지 체크
		for (int i = 0; i < cart.getProducts().size(); i++) {
			Cart.Product basketProduct = cart.getProducts().get(i);
			boolean isPriceChanged = false;
			Product product = productMap.get(basketProduct.getId());
			if (product == null) {
				cart.getProducts().remove(basketProduct);
				i--;
				cart.addMessage(basketProduct.getName() + " 상품이 삭제되었습니다.");
				continue;
			}
			// if (!product.getPrice().equals(productMap.get(product.getId()).getPrice())) {
			// 	isPriceChanged = true;
			// 	basketProduct.setPrice(product.getPrice());
			// }`
			Map<Long, ProductItem> productItemMap = product.getProductItems().stream()
				.collect(Collectors.toMap(ProductItem::getId, productItem -> productItem));
			List<String> tmpMessages = new ArrayList<>();
			for (int j = 0; j < basketProduct.getItems().size(); j++) {
				Cart.ProductItem basketProductItem = basketProduct.getItems().get(j);
				ProductItem pi = productItemMap.get(basketProductItem.getId());
				if (pi == null) {
					basketProduct.getItems().remove(basketProductItem);
					j--;
					tmpMessages.add(basketProductItem.getName() + " 옵션이 삭제되었습니다.");
					continue;
				}
				boolean isCountNotEnough = false;
				if (basketProductItem.getCount() > productItemMap.get(basketProductItem.getId())
					.getCount()) {
					isCountNotEnough = true;
					basketProductItem.setCount(pi.getCount());
				}
				if (isPriceChanged && isCountNotEnough) {
					tmpMessages.add(
						basketProductItem.getName() + " 가격변동, 수량 부족으로 구매 가능한 최대치로 변경되었습니다.");
				} else if (isPriceChanged) {
					tmpMessages.add(basketProductItem.getName() + " 가격이 변동되었습니다.");
				} else if (isCountNotEnough) {
					tmpMessages.add(basketProductItem.getName() + " 수량이 부족하여 구매 가능한 최대치로 변경되었습니다.");
				}
			}
			if (basketProduct.getItems().size() == 0) {
				cart.getProducts().remove(basketProduct);
				i--;
				cart.addMessage(basketProduct.getName() + " 상품의 옵션이 모두 없어져 구매가 불가능 합니다.");
				continue;
			} else if (tmpMessages.size() > 0) {
				StringBuilder builder = new StringBuilder();
				builder.append(basketProduct.getName() + "상품의 변동 사항 : ");
				for (String message : tmpMessages) {
					builder.append(message);
					builder.append(", ");
				}
				cart.addMessage(builder.toString());
			}
		}
		return cart;
	}

	private boolean addAble(Cart cart, Product product, AddProductCartForm form) {
		Cart.Product cartProduct = cart.getProducts()
			.stream()
			.filter(p -> p.getId().equals(form.getId()))
			.findFirst()
			.orElse(Cart.Product.builder().id(product.getId()).items(Collections.emptyList()).build());
		Map<Long, Integer> cartItemCountMap = cartProduct.getItems().stream()
			.collect(Collectors.toMap(Cart.ProductItem::getId, Cart.ProductItem::getCount));
		Map<Long, Integer> currentItemCountMap = product.getProductItems().stream()
			.collect(Collectors.toMap(ProductItem::getId, ProductItem::getCount));
		return form.getItems().stream().noneMatch(
			formItem -> {
				Integer cartCount = cartItemCountMap.get(formItem.getId());
				if (cartCount == null) {
					cartCount = 0;
				}
				Integer currentCount = currentItemCountMap.get(formItem.getId());
				return formItem.getCount() + cartCount > currentCount;
			});
	}
}
