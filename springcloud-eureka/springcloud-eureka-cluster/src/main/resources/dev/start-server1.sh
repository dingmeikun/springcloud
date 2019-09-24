nohup java -server -Xmx512m -Xms512m -Xloggc:/logdata/eureka-onlineteam-server/gc.log -jar eureka-onlineteam-server-4.0.0.jar  --spring.profiles.active=eurekaserver1 >/dev/null 2>&1 &
#10.0.7.23