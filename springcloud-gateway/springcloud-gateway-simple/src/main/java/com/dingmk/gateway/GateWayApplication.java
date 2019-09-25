package com.dingmk.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

@SpringBootApplication
public class GateWayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GateWayApplication.class, args);
	}
	
	// 路由规则提供两种方式：1 可以是使用代码指定注入Bean(routeLocator)并且指定路由规则 2也可以是在配置文件application.yml中配置路由规则(推荐)
	@Bean
	private RouteLocator testRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				// 1.转发/jd/**开头的的请求，到http://jd.com:80/，其中断言部分就是predicates: - Path=/jd/**
				.route("test_id", r -> r.path("/jd/**")
					.uri("http://jd.com:80/"))
				
				// 2.转发/test开头的请求，到http://localhost:8071/test/head上面，并在转发时增加请求头头参数：X-Request-Acme=ValueB
				.route("add_request_header_route", r -> r.path("/test")
					.filters(f -> f.addRequestHeader("X-Request-Acme", "ValueB"))
					.uri("http://localhost:8071/test/head"))
				
				// 3.转发/addRequestParameter的请求到服务http://localhost:8071/test/addRequestParameter上，并增加请求参数example=ValueB
				.route("add_request_parameter_route", r -> r.path("/addRequestParameter")
					.filters(f -> f.addRequestParameter("example", "ValueB"))
					.uri("http://localhost:8071/test/addRequestParameter"))
				
				// 4.转发/foo/**的请求到http://www.baidu.com，并重写请求URI(将/foo/路径去掉)[访问：localhost:8080//foo/cache/article/192313.html，转发：http://www.baidu.com/cache/article/192313.html]
				.route("rewritepath_route", r -> r.path("/foo/**")
					.filters(f -> f.rewritePath("/foo/(?<segment>.*)","/$\\{segment}"))
					.uri("http://www.baidu.com"))
				
				// 5.转发/test到http://www.baidu.com，并在相应头加入参数：X-Response-Foo=Bar，一并返回给客户端
				.route("add_request_header_route", r -> r.path("/test")
					.filters(f -> f.addResponseHeader("X-Response-Foo", "Bar"))
					.uri("http://www.baidu.com"))
				
				// 6.转发/test/retry到http://localhost:8071/retry?key=abc&count=2中，并在不响应时重试2次，成功返回，否则返回500(服务器内部错误)给客户端
				.route("retry_route", r -> r.path("/test/retry")
					.filters(f ->f.retry(config -> config.setRetries(2).setStatuses(HttpStatus.INTERNAL_SERVER_ERROR)))
					.uri("http://localhost:8071/retry?key=abc&count=2"))
				.build();
	}
	
	
}
