package com.dingmk.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 统一网关入口
 * 动态路由规则：
 * 	默认本地缓存1分钟，远程缓存1天，数据落到MySQL(只有下游服务状态：下线、上线、更新时，多节点才会有不一致的情况)
 * 	1.设置自动轮询刷新DB->Redis,轮询间隔为10分钟
 * 	2.设置手动刷新DB->Redis,可人为使用命令刷新缓存
 * 
 * @author dingmeikun
 *
 */
@EnableScheduling
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.dingmk"})
public class GateWayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GateWayApplication.class, args);
	}
}
