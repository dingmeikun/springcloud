package com.dingmk.config.controller;

import javax.annotation.Resource;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dingmk.config.configure.ClinetConfigBusProperties;

@RefreshScope
@RestController
@RequestMapping("configConsumer")
public class ClientConfigController {

	@Resource
	private ClinetConfigBusProperties properties;
	
	@RequestMapping("/getconfigInfo")
	public String getConfigInfo() {
		return properties.getConfig();
	}
}
