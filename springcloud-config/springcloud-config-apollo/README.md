## Apollo应用实战
apollo在线配置平台，支持热发布，实时生效等特性。

### 部署步骤
将项目打包(zip包)，并放置到服务器执行。注意需要先运行configservice，adminservice，portal三个服务，他们端口分别是8070，8080，8090

#### 服务包下载
部署包详见github

	https://github.com/ctripcorp/apollo/releases
	
apollo部署后，可以使用自带的项目，也可新建项目，appid设置为123456789，并创建配置config，同时记得发布！(不发布将看不到结果)
#### 部署包构建
直接对本项目打包即可：clean install(package)，之后部署项目，执行无报错即可。

#### 测试效果
在浏览器访问：http://192.168.101.218:8018/configConsumer/getConfigInfo