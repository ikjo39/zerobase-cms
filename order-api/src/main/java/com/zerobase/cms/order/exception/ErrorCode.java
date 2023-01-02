package com.zerobase.cms.order.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	SAME_ITEM_NAME(HttpStatus.BAD_REQUEST, "아이템명 중복입니다."),
	CART_FAIL_CHANGE(HttpStatus.BAD_REQUEST, "장바구니에 추가할 수 없습니다."),
	ITEM_COUNT_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "상품의 수량이 부족합니다."),
	NOT_FOUND_PRODUCT(HttpStatus.BAD_REQUEST, "상품이 존재하지 않습니다."),
	NOT_FOUND_PRODUCT_ITEM(HttpStatus.BAD_REQUEST, "상품 아이템이 존재하지 않습니다."),

	ORDER_FAIL_CHECK_CART(HttpStatus.BAD_REQUEST, "주문 불가! 장바구니를 확인해주세요."),
	ORDER_FAIL_NO_MONEY(HttpStatus.BAD_REQUEST, "주문 불가! 잔액 부족입니다.");

	private final HttpStatus httpStatus;
	private final String detail;
}
