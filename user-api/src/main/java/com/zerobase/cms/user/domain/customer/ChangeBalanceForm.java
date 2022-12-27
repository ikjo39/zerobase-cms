package com.zerobase.cms.user.domain.customer;

import lombok.Getter;

// 출금과 입금을 같은 폼으로 취급
@Getter
public class ChangeBalanceForm {
	private String from;
	private String message;
	private Integer money;
}
