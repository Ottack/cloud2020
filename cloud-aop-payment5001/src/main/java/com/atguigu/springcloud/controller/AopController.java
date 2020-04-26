package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class AopController {

	@Resource
	private UserService userService;

	@GetMapping(value = "/aop")
	public void  AopTest()
	{
		userService.insertUser();
	}
}
