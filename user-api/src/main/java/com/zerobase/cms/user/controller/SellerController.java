package com.zerobase.cms.user.controller;

import com.zerobase.cms.user.application.SignUpApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signUp")
@RequiredArgsConstructor
public class SellerController {

	private final SignUpApplication signUpApplication;



}