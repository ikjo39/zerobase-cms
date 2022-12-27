package com.zerobase.cms.user.service;


import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.service.customer.SignUpCustomerService;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// test는 추가 선택
// SignUpForm 같이 @Getter 만 만들면 필드들 injection이 어려움
// 테스트 코드 작성시 특정 필드에 대하여 when then injection 해야함
// @Getter로 하려면 조금 귀찮을 수 있음
// 테스트가 중점이 될건지 한번 생각해보기
// 일단 builder를 씀
// Mocking 해서 쓰는게 정석이긴 함 - 시간 소요가 많이 걸림
@SpringBootTest
class SignUpCustomerServiceTest {

	@Autowired
	private SignUpCustomerService service;

	@Test
	void signUp() {
		SignUpForm form = SignUpForm.builder()
			.name("name")
			.birth(LocalDate.now())
			.email("abc@gmail.com")
			.password("1")
			.phone("01000000000")
			.build();
		Customer c = service.signUp(form);
		assertNotNull(c.getId());
		assertNotNull(c.getCreatedAt());
	}
}