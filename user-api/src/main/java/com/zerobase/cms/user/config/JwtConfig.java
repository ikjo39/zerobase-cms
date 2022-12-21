package com.zerobase.cms.user.config;

import com.zerobase.cms.domain.config.JwtAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//다른 모듈에서 끌고왔기에 빈이 자동으로 생성이 안됨
@Configuration
public class JwtConfig {

	@Bean
	public JwtAuthenticationProvider jwtAuthenticationProvider() {
		return new JwtAuthenticationProvider();
	}
}
