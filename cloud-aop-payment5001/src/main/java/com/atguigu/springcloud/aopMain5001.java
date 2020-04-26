package com.atguigu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class aopMain5001 {
	public static void main(String[] args) {
		SpringApplication.run(aopMain5001.class,args);
	}
}
