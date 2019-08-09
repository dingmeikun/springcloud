package com.dingmk.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ZuulSimpleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulSimpleApplication.class, args);
	}
}