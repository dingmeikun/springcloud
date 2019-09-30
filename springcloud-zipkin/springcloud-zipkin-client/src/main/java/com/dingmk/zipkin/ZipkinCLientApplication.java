package com.dingmk.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ZipkinCLientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZipkinCLientApplication.class, args);
	}
}
