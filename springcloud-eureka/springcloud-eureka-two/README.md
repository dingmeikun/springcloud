# springcloud-eureka-two(本地eureka注册中心)双节点
eureka注册中心双节点集群，以便对外提供服务能力。

## 使用步骤
1 先做本地域名映射，在Windows的C盘的hosts或者Linux的/etc/hosts文件中，做以下域名映射：

	127.0.0.1 dingmk1.com
	127.0.0.1 dingmk2.com
	
2 在application.yml文件中做多节点配置：

	---
	## node 1
	spring: 
	  profiles: dingmk1.com
	server: 
	  port: 8888
	  
	eureka: 
	    instance: 
	        hostname: dingmk1.com #本机域名转换为127.0.0.1 dingmk1.com和127.0.0.1 dingmk2.com
	        ipAddress: 192.168.101.15 #本机IP
	        instance-id: dingmk1.com:${server.port}
	        preferIpAddress: false
	    client: 
	        registerWithEureka: true #是否将自己注册到eureka server
	        fetchRegistry: true #是否从eureka集群获取注册信息
	        serviceUrl: 
	            defaultZone: http://dingmk2.com:8889/eureka
	
	---
	## node 2
	spring: 
	  profiles: dingmk2.com
	server: 
	  port: 8889
	  
	eureka: 
	    instance: 
	        hostname: dingmk2.com
	        ipAddress: 192.168.101.15
	        instance-id: dingmk2.com:${server.port}
	        preferIpAddress: false
	    client: 
	        registerWithEureka: true
	        fetchRegistry: true
	        serviceUrl: 
	            defaultZone: http://dingmk1.com:8888/eureka
	            
3 启动类com.dingmk.eureka.Application，并附带启动参数：--spring.profiles.active=dingmk1.com，再次启动，并附带启动参数：--spring.profiles.active=dingmk2.com 启动两个JVM挂载的eureka注册中心服务相互注册

4 分别访问：127.0.0.1:8888 和 127.0.0.1:8889 访问两个注册中心界面。
