package com.atguigu.springcloud.Controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.lb.LoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.URI;
import java.util.List;


@RestController
@Slf4j
public class OrderControl {

	public static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";

	@Resource
	private RestTemplate restTemplate;

	@Resource
	private LoadBalancer loadBalancer;

	@Resource
	private DiscoveryClient discoveryClient;

	@PostMapping("/consumer/payment/create")
	public CommonResult<Payment> create(@RequestBody Payment payment) {
		return restTemplate.postForObject(PAYMENT_URL + "/payment/create", payment, CommonResult.class
		);
	}

	@GetMapping("/consumer/payment/get/{id}")
	public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id) {
		return restTemplate.getForObject(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);
	}

	@GetMapping("/consumer/payment/getForEntity/{id}")
	public CommonResult<Payment> getPayment2(@PathVariable("id") Long id) {
		ResponseEntity<CommonResult> entity = restTemplate.getForEntity(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);
		if (entity.getStatusCode().is2xxSuccessful()) {
			return entity.getBody();
		} else {
			return new CommonResult<>(444, "操作失败");
		}
	}

	@PostMapping("/consumer/payment/createForEntity")
	public CommonResult<Payment> create2(@RequestBody Payment payment) {
		ResponseEntity<CommonResult> result = restTemplate.postForEntity(PAYMENT_URL + "/payment/create", payment, CommonResult.class
		);
		if (result.getStatusCode().is2xxSuccessful()) {
			return result.getBody();
		} else {
			return new CommonResult<>(444, "插入失败");
		}

	}

	@GetMapping(value = "/consumer/payment/lb")
	public String GetPaymentLB() {
		List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
		if (instances.isEmpty()) return null;

		ServiceInstance serviceInstance = loadBalancer.getInstances(instances);

		URI uri = serviceInstance.getUri();

		return restTemplate.getForObject(uri + "/payment/lb/", String.class);
	}

	@GetMapping(value = "/consumer/payment/zipkin")
	public String consumerZipkin() {
		String result = restTemplate.getForObject(PAYMENT_URL + "/payment/zipkin/", String.class);
		return result;
	}

}
