package com.dingmk.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author dingmeikun
 * @see 目前版本1.0.0，项目从springboot-1.5.2 D版本升级到springboot-2.0.9 F版本，中间支持disconf有点问题，原因在于新版disconf去除了一个方法parseStringValue，详情可参考：https://blog.csdn.net/ai_xao/article/details/86525196
 * 
 * @see 使用过程中需要注意
 * 	1.启动类GateWayApplication需要增加包扫描配置注解：@ComponentScan(basePackages = {"com.dingmk"}),以防disconf对当前项目配置类不能读取
 * 	2.配置类DisconfConfig需要随当前项目的包名前缀，以及部分集成过后的框架包(tim-database,tim-redis-cache默认是：xy.dingmk)一致，否则导致disconf不能扫描到配置类，以及框架包内的配置类
 *
 *	@see 当前的问题
 *	1.缓存框架还未做迁移，项目启动将会报错：XyCacheManager的bean找不到！因为缓存框架xy-configuration-cache-redis的默认路径包是：com.xy，需要引入自己的包(tim-redis-cache:xy.dingmk)
 *	
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.dingmk"})
public class GateWayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GateWayApplication.class, args);
	}
}
