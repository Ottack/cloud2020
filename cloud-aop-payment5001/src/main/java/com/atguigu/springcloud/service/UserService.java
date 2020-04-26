package com.atguigu.springcloud.service;

import org.springframework.stereotype.Service;

@Service
public class UserService {

	public void insertUser() {
		System.out.println("插入用户成功");
	}

	public boolean updateUser() {
		System.out.println("更新用户成功");
		return true;
	}
}
