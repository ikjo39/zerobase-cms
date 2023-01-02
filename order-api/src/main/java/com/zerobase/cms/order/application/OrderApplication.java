package com.zerobase.cms.order.application;

import static com.zerobase.cms.order.exception.ErrorCode.*;

import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zerobase.cms.order.client.UserClient;
import com.zerobase.cms.order.client.user.ChangeBalanceForm;
import com.zerobase.cms.order.client.user.CustomerDto;
import com.zerobase.cms.order.domain.model.ProductItem;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.exception.CustomException;
import com.zerobase.cms.order.service.ProductItemService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderApplication {
	private final ProductItemService productItemService;
	private final CartApplication cartApplication;
	private final UserClient userClient;

	@Transactional
	public void order(String token, Cart cart) {
		// 1. 주문시 기존 카트를 버림
		// 2. 선택주문: 내가 사지 않은 아이템을 살려야함
		// ---> 2번은 숙제
		Cart orderCart = cartApplication.refreshCart(cart);
		if (orderCart.getMessages().size() > 0) {
			// 문제가 있음
			throw new CustomException(ORDER_FAIL_CHECK_CART);
		}
		CustomerDto customerDto = userClient.getCustomerInfo(token).getBody();
		Integer totalPrice = getTotalPrice(cart);
		if (customerDto.getBalance() < totalPrice) {
			throw new CustomException(ORDER_FAIL_NO_MONEY);
		}
		// 결제 하는 타이밍에 잔액이 부족할때 롤백하는 전략을 생각해야함
		userClient.changeBalance(token, ChangeBalanceForm.builder()
			.from("USER")
			.message("Order")
			.money(-totalPrice)
			.build());
		for (Cart.Product product : orderCart.getProducts()) {
			for (Cart.ProductItem cartItem : product.getItems()) {
				ProductItem productItem = productItemService.getProductItem(cartItem.getId());
				productItem.setCount(productItem.getCount() - cartItem.getCount());
			}
		}
	}

	private Integer getTotalPrice(Cart cart) {
		return cart.getProducts().stream().flatMapToInt(
			product -> product.getItems().stream().flatMapToInt(
				productItem -> IntStream.of(productItem.getPrice() * productItem.getCount()))).sum();
	}
	// 결제를 위해 필요한 것
	// 1. 물건들이 전부 주문 가능한 상태인지 확인
	// 2. 가격 변동이 있었는지에 대해 확인
	// 3. 고객의 돈이 충분한지.
	// 4. 결재 & 상품의 재고 관리
}
