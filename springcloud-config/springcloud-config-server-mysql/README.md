### spring config for mysql
此为配置MySQL为数据源，将配置信息配置到mysql，通过springcloud config读取和刷新mysql对应表数据，拿到配置

#### 操作步骤
(1)添加mysql依赖

	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-jdbc</artifactId>
	</dependency>
	<dependency>
		<groupId>mysql</groupId>
		<artifactId>mysql-connector-java</artifactId>
	</dependency>
	
(2)配置数据源

	spring:
	   application:
	      name: config-server-mysql-repo
	   cloud:
	      config:
	         server:
	            jdbc:
	               sql: select `key`, `value` from properties where application = ? and profile = ? and lable = ?;
	         label: master
	      refresh: 
	         refreshable: none
	   profiles:
	      active: jdbc
	   # 数据源配置
	   datasource:
	      url: jdbc:mysql://192.168.101.15:3306/spring_config?useUnicode=true&characterEncoding=utf-8
	      username: dingmk
	      password: 123456
	      driver-class-name: com.mysql.jdbc.Driver
	      
之后，启动即可！