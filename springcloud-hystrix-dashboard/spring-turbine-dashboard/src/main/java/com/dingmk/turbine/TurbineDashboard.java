package com.dingmk.turbine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableTurbine
@EnableEurekaClient
@EnableDiscoveryClient
@ComponentScan(basePackages = { "com.dingmk" })
public class TurbineDashboard {

	public static void main(String[] args) {
		SpringApplication.run(TurbineDashboard.class, args);
	}

}
