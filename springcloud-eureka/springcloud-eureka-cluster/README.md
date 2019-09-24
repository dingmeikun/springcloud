# eureka-onlineteam-server(eureka注册中心)
eureka注册中心，接入陌电或其他项目中涉及到微服务注册功能，以便对外提供服务能力。


## 编写目的
描述eureka注册中心集群配置和部署步骤。本文档使用人员：运维人员、开发人员。

## 术语及定义
术语|定义|
--|:--:|
注册中心|提供服务的进行注册、发现、监控等功能的服务框架|

## 安装环境

### 1.硬件环境
* 服务器：Redhat/CentOS7 512mRAM

### 2.软件环境
确定服务机器以及端口可用:

* 机器一 10.30.193.206:8889 (eurekaserver1)
* 机器二 10.47.99.229:8889 (eurekaserver2)
* 机器三 10.30.193.99:8889 (eurekaserver3)
* 机器四 10.24.39.42:8889 (eurekaserver4)
* 机器五 10.24.38.88:8889 (eurekaserver5)

### 3.准备工作
#### 配置服务器域名
按照以上服务器列表，修改各服务器hosts文件，增加域名配置。

	# 配置机器域名,示例：10.30.193.206 (eurekaserver1)
	$ sudo ssh 10.30.193.206
	$ vim /etc/hosts
		10.30.193.206 eurekaserver1

		10.47.99.229 eurekaserver2
		10.30.193.99 eurekaserver3
		10.24.39.42 eurekaserver4
		10.24.38.88 eurekaserver5

	# 按照以上示例，分别对其余四个IP：10.47.99.229、10.30.193.99、10.24.39.42、10.24.38.88 设置域名：
		10.30.193.206 eurekaserver1
		10.47.99.229 eurekaserver2
		10.30.193.99 eurekaserver3
		10.24.39.42 eurekaserver4
		10.24.38.88 eurekaserver5


### 4.软件包准备
包名称|版本|来源|备注|
--|:--:|--:|:--:|
eureka-onlineteam-server-4.0.0.zip|4.0.0|http://git.mfexcel.com/archive/eureka-onlineteam-server/blob/master/eureka-onlineteam-server-4.0.0.zip|安装包|

## 安装步骤

### 1.配置

* 提前配置好域名

### 2.部署
#### (1)上传部署包
在**安装环境·4软件包**注有具体的软件包链接，直接执行以下命令执行命令。

    $ cd /apps/eureka-onlineteam-server
    $ sudo wget http://git.mfexcel.com/archive/eureka-onlineteam-server/blob/master/eureka-onlineteam-server-4.0.0.zip
    $ sudo unzip eureka-onlineteam-server-4.0.0.zip
    
#### (2)检查配置
解压之后的文件中，需要检查脚本**application.yml**，**logback-spring.xml**，**start-server1.sh**，**start-server2.sh**，**start-server3.sh**，**start-server4.sh**，**start-server5.sh**，**stop.sh**，**taillog.sh**查看其中的各项配置是否正确，配置是否和当前系统环境一致,具体参数可按实际情况设置。
其中文件和服务器对应关系是：

	10.30.193.206 启动脚本为：start-server1.sh
	10.47.99.229 启动脚本为：start-server2.sh
	10.30.193.99 启动脚本为：start-server3.sh
	10.24.39.42 启动脚本为：start-server4.sh
	10.24.38.88 启动脚本为：start-server5.sh

#### (3)启动脚本
在确认配置无误，分别在以下机器执行命令启动服务：

	机器一 10.30.193.206
    $ sudo sh start-server1.sh

	机器二 10.47.99.229
    $ sudo sh start-server2.sh

	机器三 10.30.193.99
    $ sudo sh start-server3.sh

	机器四 10.24.39.42
    $ sudo sh start-server4.sh

	机器五 10.24.38.88
    $ sudo sh start-server5.sh

	启动完成后，可启动日志脚本，查看运行日志
	$ sudo sh taillog.sh
    
### 3.确认
#### (1)查看进程
查看当前进程是否开启，可以通过ps命令查看进程的实例信息，实例名在文件**application.yml**中设置为：

    $ spring.application.name=eureka-onlineteam-server

然后再查看当前进程信息：

    $ ps -ef|grep eureka-onlineteam-server
    
如果能看到启动用户、进程ID、启动时间和启动命令，说明服务进程已经开启，但需要进一步确认确认日志是否有报错。

### (2)查看日志
去到上面五台机器查看启动日志。打开日志配置文件**logback-spring.xml**，翻看配置项**log_home**和**module_name**:

    <property name="LOG_HOME" value="/logdata/eureka-onlineteam-server"/>
    <property name="MODULE_NAME" value="eureka-onlineteam-server"/>
    
然后分别去到路径`/logdata/eureka-onlineteam-server`下查看日志是否出现异常，且五个服务是否都是up状态。

    $ sudo vim eureka-onlineteam-server_Running.log
    
### (3)确认状态
在浏览器分别访问：http://10.30.193.206:8889，http://10.47.99.229:8889,http://10.30.193.99:8889,http://10.24.39.42:8889,http://10.24.38.88:8889，确认是否能正确访问，并且在application位置能看到服务实例：EUREKA-ONLINETEAM-SERVER，以及已经注册好了的ip端口信息，并且General Info中available-replicas有值。