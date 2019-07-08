package com.dingmk.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@EnableEurekaClient
@SpringBootApplication
@ComponentScan(basePackages = { "com.dingmk.eureka" })
public class EurekaService {

	public static void main(String[] args) {
		SpringApplication.run(EurekaService.class, args);
	}

}
