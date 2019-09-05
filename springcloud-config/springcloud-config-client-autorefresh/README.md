### auto refresh config
此为自动刷新spring-config-client客户端缓存

#### 操作步骤
此为一个组件，需要依赖在其他的springconfig-client上生效。

(1)客户端增加依赖

	<dependency>
		<groupId>com.dingmk.learn</groupId>
		<artifactId>springcloud-config-client-autorefresh</artifactId>
		<version>1.0.0</version>
	</dependency>
	
(2)客户端配置文件增加刷新时间

	spring: 
   		application:
      		name: config-client-mysql-repo
   		cloud:
      		config:
         		refreshInterval: 10 #单位秒(s),每10秒刷新