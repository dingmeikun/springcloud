nohup java -server -Xmx512m -Xms512m -Xloggc:/logdata/springcloud-eureka-cluster/gc.log -jar springcloud-eureka-cluster-1.0.0.jar  --spring.profiles.active=eurekaserver5 >/dev/null 2>&1 &
#10.24.38.88