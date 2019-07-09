package com.dingmk.ribbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@EnableEurekaClient
@SpringBootApplication
@ComponentScan(basePackages = { "com.dingmk" })
public class RibbonClient {

	public static void main(String[] args) {
		SpringApplication.run(RibbonClient.class, args);
	}

}
