springcloud-hystrix-dashboard项目

项目运行起来，执行以下命令访问服务：

```javascript
curl -H "Content-Type: application/json" -XPOST http://192.168.101.15:8007/data/feign/query/v1 -d '{"ip":"111.111.111.112","location":{"coordsys":"","lat":"20.027870","lng":"110.162315"}}'
```

在此期间，可访问hystrixdashboard查看监控数据：

```javascript
http://localhost:8007/hystrix/monitor?stream=http%3A%2F%2Flocalhost%3A8007%2Fhystrix.stream
```

