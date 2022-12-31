package com.zerobase.cms.order.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	ALREADY_REGISTER_USER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
	SAME_ITEM_NAME(HttpStatus.BAD_REQUEST, "아이템명 중복입니다."),

	NOT_FOUND_PRODUCT(HttpStatus.BAD_REQUEST, "상품이 존재하지 않습니다."),
	NOT_FOUND_PRODUCT_ITEM(HttpStatus.BAD_REQUEST, "상품 아이템이 존재하지 않습니다.");
	private final HttpStatus httpStatus;
	private final String detail;
}
