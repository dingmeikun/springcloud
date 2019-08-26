# springcloud-zuul-ratelimit-server Zuul限流

zuul限流使用的是ratelimit令牌桶算法，实现流量的控制，可以实现请求的QPS限制。

	#  执行命令
	curl -H "Content-Type: application/json" -XPOST http://192.168.101.15:8013/service/zuul/v1 -d '{"ip":"111.111.111.000","location":{"coordsys":"","lat":"20.027870","lng":"110.162315"}}'
	
### 压测
ratelimit zuul中的limit设置为10，然后使用代码，或者postman压测10个，将会有返回报错，错误码:429 Too Many Request