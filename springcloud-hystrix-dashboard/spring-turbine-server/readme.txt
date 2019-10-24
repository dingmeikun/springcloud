服务熔断展示面板，用于收集服务调用的qps并展示，如果服务被熔断则会出现红色并显示close

1.使用很简单，application.yml文件需要设置待监控的服务id(serviceid)

	turbine:
  		app-config: eureka-service,turbine-node1,turbine-node2
  		
2.当所有服务都气起来了，则启动当前turbine监控服务，并访问页面：http://192.168.101.15:9709/hystrix

3.当登录之后，再访问：http://192.168.101.15:9709/turbine.stream 查看熔断信息，当前时为空页面

4.尝试着请求turbine-node1,turbine-node2两个服务，将出现监控页面的动态监控效果。