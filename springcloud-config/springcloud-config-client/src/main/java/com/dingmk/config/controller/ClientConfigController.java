package com.dingmk.config.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dingmk.config.configure.ClinetConfigGitProperties;

@RestController
public class ClientConfigController {

	@Resource
	private ClinetConfigGitProperties properties;
	
	@RequestMapping("/getconfigInfo")
	public String getConfigInfo() {
		return properties.getConfig();
	}
}
