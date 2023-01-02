package com.zerobase.cms.order.client.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 출금과 입금을 같은 폼으로 취급
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeBalanceForm {
	private String from;
	private String message;
	private Integer money;
}
