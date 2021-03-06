# admin部署文档

## 功能用途

admin项目是监控线上微服务调用的一个服务组件，主要用于查看服务内存，堆栈，服务进程等，下线提醒等功能。
该服务不对外提供服务，主要用于内部监控。
ps : 本次不包含服务进程下线告警提示功能。

## 编写目的

描述admin的配置细节和部署步骤。本文档使用人员：运维人员、开发人员。

## 模块名称（service）

1. admin-server 服务

## 应用类别：服务

## 部署路径

- 部署路径：/apps/adminserver/
- 业务日志路径：/logdata/adminserver/
- 打点日志路径：无

## 配置文件(如涉及配置修改请注明)

1.本次部署依赖Eureka，需正式eureka连接，并配置到对应application-prod.yml文件

```
# eureka注册中心配置
eureka: 
	client:
        serviceUrl:
			defaultZone: http://172.30.0.157:8889/eureka,http://172.30.0.158:8889/eureka,http://172.30.0.159:8889/eureka,http://172.30.0.160:8889/eureka,http://172.30.0.161:8889/eureka,http://172.30.0.162:8889/eureka
```

### 启动说明(如多模块有启动顺序需说明)

1.启动jvm参数已配置到脚本 ctrl.sh，具体可修改当中部分参数。
  建议jvm内存设置为1G，示例：jvm_mem='-Xms1024m -Xmx1024m -Xmn512m'

2.登录界面需要输入密码，配置密码的地方在application-prod.yml

```xml
security:
    user:
      name: admin
      password: xy123456
```

### 模块相关URI(附所有URL全地址)

#### 外网正式环境

不对外提供服务

#### 外网测试环境
不对外提供服务

## 项目依赖说明（如涉及调用第三方或被外部调用请注明）
无
#### 受限第三方IP白名单（如为是，需指定部署服务器IP）：否

#### 公共组件依赖（disconf & 授权中心 & zookeeper  & eureka）

1.项目依赖eureka服务，具体按照线上eureka服务地址配置到application-prod.yml
2.部署文件application.yml 用于指定哪个环境（dev: 开发  ，test: 测试，prod : 生产），本次部署需要检查是否配置为生产环境：

```
spring:
  profiles:
    active: prod
```

#### 部署前置条件(运营系统需提前执行的操作)：无

#### 其他扩展依赖(如为是，需指定部署说明)：无

## 本次变更说明
#### 本次变更内容（简单描述本次变更）
​	1.本项目为新增项目，无需做nginx映射。
#### 部署步骤(按具体项目情况描述)

1. 检查受检文件对象（application.yml,application-prod.yml) 
2. 启动程序。（程序启动端口号为3004）
3. 启动之后登陆：http:ip:3004   输入密码即可查看(admin/xy123456)。
4. 建议登录的相关ip端口转发出来，方便在内网可以使用web页面访问查看。