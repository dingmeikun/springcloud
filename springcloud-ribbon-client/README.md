springcloud-ribbon-client项目

微服务-ribbon负载均衡调用，当应用启动，可使用curl访问其url进行服务请求：

```js
curl -H "Content-Type: application/json" -XPOST http://192.168.101.15:8004/data/ribbon/query/v1 -d '{"ip":"111.111.111.112","location":{"coordsys":"","lat":"20.027870","lng":"110.162315"}}'
```

