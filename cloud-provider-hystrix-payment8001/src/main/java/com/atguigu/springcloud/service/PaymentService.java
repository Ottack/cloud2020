package com.atguigu.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {
	/*

	 */
	public String paymentInfo_OK(Integer id) {
		return "线程池：  " + Thread.currentThread().getName() + "  paymentINfo_OK,id:   " + "\t" + "OK";
	}

	@HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2500")
	})
	public String paymentInfo_TimeOut(Integer id) {
		int timeNumber = 3;

		try {
			TimeUnit.SECONDS.sleep(timeNumber);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//int a = 10/0;
		return "线程池：  " + Thread.currentThread().getName() + "  paymentINfo_TimeOut,id:   " + "\t" + "TimeOut" ;
	}

	public String paymentInfo_TimeOutHandler(Integer id) {
		return "线程池：  " + Thread.currentThread().getName() + "  ,8001系统繁忙:   " + "\t" + "TimeOutHandler";

	}

//	1、断路器设置Enable，断路器开启的第一个条件
//	2、设置请求阀值，滚动时间窗内（默认是10S）请求次数超过阀值，才能满足断路器开启的第二个条件
//	3、设置失败率，默认是50%，滚动时间窗内（默认是10S）请求的次数，失败率到达设定值，满足断路器开启的第三个条件，此时断路器开启
//	4、在断路器开启后，会有一个沉睡时间窗，沉睡结束后，断路器会进去一个half-open状态，此时会尝试调用后端的微服务
//	5、若此时调用成功，则断路器回到close状态，服务恢复正常访问
//	6、若此时调用仍然失败，断路器回到open状态，并且返回此时的fallback结果，继续下一个沉睡期
	@HystrixCommand(
			fallbackMethod = "paymentCircuitBreaker_fallback"
			,
			commandKey = "HystrixPayment",groupKey = "HystrixPaymentGroup", threadPoolKey = "HystrixPaymentGroup8001",
			commandProperties = {
					@HystrixProperty(name = "circuitBreaker.enabled", value = "true"),  //是否开启断路器
//					@HystrixProperty(name = "metrics.rollingStats.timeinMilliseconds", value = "20000"),  //滚动时间窗
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),  //请求次数(请求阀值)
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"), //失败率达到多少熔断
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), //休眠时间窗
			}
			)
	public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
		if (id < 0) {
			throw new RuntimeException("****id不能为负数，id: " + id);
		}
		String serialNumber = IdUtil.simpleUUID();
		return Thread.currentThread().getName() + "\t" + "调用成功，流水号：" + serialNumber;
	}

	public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id) {
		return "id不能为负数，paymentCircuitBreaker_fallback，请稍后再试！ id: " + id;
	}
}
