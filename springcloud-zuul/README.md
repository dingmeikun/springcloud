#### springcloud-zuul模块项目
模块分为：
* springcloud-zuul-server
* springcloud-zuul-simple

##### springcloud-zuul-server zuul服务模块
server模块主要做服务分发的功能，对外提供总的服务入库，所有后端服务请求都往我这里来！！再由我分发给对应路由规则的服务中去。通过eureka进行服务注册管理发现，由server的routes规则，发现其中的serviceId并转发到具体的server url中去。  
服务示例：

	curl -H "Content-Type: application/json" -XPOST http://192.168.101.15:8009/data/service/zuul/v1 -d '{"ip":"111.111.111.000","location":{"coordsys":"","lat":"22.299102","lng":"114.186916"}}'
	
解析：服务由eureka-server、zuul-server、zuul-simple三个简单项目组成。
	eureka-server:提供springcloud微服务的注册和发现
	zuul-server：做服务路由功能，约定本服务中的各个路由规则，已经该路由到哪个具体的serviceId中去
	zuul-simple：简单微服务，注册到eureka-server，启动并对外提供服务。
	
过程：外部客户端都是请求的zuul-server，由zuul-server的routes规则节点配置路由，转到zuul-simple的/zuul/v1中去请求，最终zuul-simple接收到请求，并处理返回。

	# 最终的过程是
	$ http://GATEWAY:GATEWAY_PORT/想要访问的Eureka服务id的小写/**     --->>>>  将会访问到--->>>       http://想要访问的Eureka服务id的小写:该服务端口/**