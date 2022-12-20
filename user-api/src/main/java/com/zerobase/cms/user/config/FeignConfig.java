package com.zerobase.cms.user.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

//	@Value(value = "${mailgun.key}")
//	private String mailgunKey;

	@Qualifier(value = "mailgun")
	@Bean
	public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
		return new BasicAuthRequestInterceptor("api", "key-8fbc80e54abc91b9db0ca8f835e8616e");
	}
}
