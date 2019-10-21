package com.dingmk.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import reactor.core.publisher.Mono;

/**
 * 当前实现了Gateway路由网关的：限流、熔断
 * 
 * @author dingmeikun
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.dingmk"})
public class GateWayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GateWayApplication.class, args);
	}
	
	@Bean
	public KeyResolver hostAddrKeyResolver() {
	    return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
	}
}
