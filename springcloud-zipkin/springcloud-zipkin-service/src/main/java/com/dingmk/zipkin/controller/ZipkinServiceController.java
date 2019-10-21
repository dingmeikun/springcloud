package com.dingmk.zipkin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dingmk.zipkin.test.LocationRequestMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ZipkinServiceController {
	
	@GetMapping("/sayHello")
    public String hello(@RequestParam("name")String name){
		
        log.info("ZipkinServiceController received. 参数: {}",name);
        String result = "hello, " + name;
        log.info("ZipkinServiceController sent. 结果: {}",result);
        return result;
    }
	
	@PostMapping("/v1/test")
	public String queryRegionLocationForV2(@RequestBody LocationRequestMessage locationRequestMessage){
		return "Yeah~";
	}
}
