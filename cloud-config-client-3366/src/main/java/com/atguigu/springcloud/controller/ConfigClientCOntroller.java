package com.atguigu.springcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@Slf4j
public class ConfigClientCOntroller {
	@Value("${config.info}")
	private String configInfo;

	@Value("${server.port}")
	private String serverInfo;

	@GetMapping(value = "/configInfo")
	public String getConfigurationInfo() {

		String msg = "serverport: " + serverInfo + "\t\n\n configinfo:" + configInfo;
		return msg;
	}
}
