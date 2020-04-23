package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class PaymentControl {
	@Resource
	private PaymentService paymentService;

	@Value("${server.port}")
	private String serverPort;

	@Resource
	private DiscoveryClient discoveryClient;

	@PostMapping(value = "/payment/create")
	public CommonResult Create(@RequestBody Payment payment) {
		int result = paymentService.create(payment);
		log.info("*****插入结果：" + result);
		if (result > 0) {
			return new CommonResult(200, "插入数据库成功,serverPort:" + serverPort, result);
		} else {
			return new CommonResult(444, "插入数据库失败" + null);
		}
	}

	@GetMapping(value = "/payment/get/{id}")
	public CommonResult getPaymentById(@PathVariable("id") Long id) {
		Payment result = paymentService.getPaymentById(id);
		log.info("*****插入结果：" + result);
		if (result != null) {
			return new CommonResult(200, "查询成功,serverPort:" + serverPort, result);
		} else {
			return new CommonResult(444, "没有对应记录,serverPort:" + id, null);
		}
	}

	@GetMapping(value = "/payment/discovery")
	public Object discovery() {
		List<String> services = discoveryClient.getServices();
		for (String s : services) {
			log.info("****** service : " + s);
		}
		List<ServiceInstance> serviceInstances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");

		for (ServiceInstance s : serviceInstances) {
			log.info(s.getServiceId() + "\t" + s.getHost() + "\t" + s.getPort() + "\t" + s.getUri());
		}
		return this.discoveryClient;
	}

	@GetMapping(value = "/payment/lb")
	public String GetPaymentLB(){
		return serverPort;
	}

	@GetMapping(value = "/payment/feign/timeout")
	public String paymentFeignTimeout(){
		try{
			TimeUnit.SECONDS.sleep(1);
		}
		catch (InterruptedException e){
			e.printStackTrace();
		}
		return serverPort;
	}

	@GetMapping(value = "/payment/zipkin")
	public String paymentZipkin(){
		return " Hello payment + zipkin + sleuth !";
	}
}
