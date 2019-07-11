## spring-turbine-dashboard项目

可集成多个springcloud微服务消费者客户端调用情况的展示话板Dashboard。

#### 模块组成

​	spring-turbine-dashboard集成展示

​	spring-turbine-node1、spring-turbine-node1调用服务

#### 启动步骤

* 启动eureka-server
* 启动eureka-service
* 启动spring-turbine-node1
* 启动spring-turbine-node2
* 启动spring-turbine-dashboard

操作步骤

* 打开google地址，输入192.168.101.15:8005/hystrix
* 访问192.168.101.15:8005/turbine.stream
* 请求spring-turbine-node1、spring-turbine-node2进行消费
* 画板展示消费者监控图