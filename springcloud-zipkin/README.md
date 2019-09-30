#### springcloud-zipkin 全链路追踪
项目包含四个模块：
* springcloud-zipkin-client 服务消费者(客户端)，去调用其他模块的服务，可调用：zipkin-service、zipkin-zuul
* springcloud-zipkin-server 链路服务，收集一个请求过程中走的所有调用链，并记录到本地内存，展示到页面：192.168.101.15:9707
* springcloud-zipkin-service 服务提供者(生产者)，对外提供服务，对外暴露uri，等待调用者请求
* springcloud-zipkin-zuul 路由服务，提供转发功能

##### 服务调用链
1 zipkin-client 请求路由服务(zipkin-zuul:/v1/**)，请求URI:v1/sayHello，请求地址：http://localhost:9709/v1/sayHello?name=999
2 zipkin-zuul通过路由规则，拿到服务实例ID：zipkin-service，并将服务转发到服务提供者(zipkin-service:/sayHello)
3 zipkin-service接收请求，响应回去
4 以上1,2,3全程记录调用过程以及调用的时间、请求ID、请求uri、请求服务器IP等，并记录本地，展示到页面：http://192.168.101.15:9707
