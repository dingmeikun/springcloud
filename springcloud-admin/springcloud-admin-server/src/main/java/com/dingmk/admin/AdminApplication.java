package com.dingmk.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

/**
 * spring admin资源监控服务，可以对服务的健康状态、cpu、线程数、磁盘空间、堆站等作出一些收集，并提供页面查看
 * 
 * 风险点：admin默认收集当前注册中心eureka的所有实例，如果某个实例没有配置spring admin的相关信息，将收集不到，并且报错
 * 
 * @author dingmeikun
 *
 */
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@EnableDiscoveryClient
@Configuration
@EnableAdminServer
public class AdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}
}
