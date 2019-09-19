nohup /opt/java/bin/java -server -Xmx2048m -Xms2048m -Xloggc:/logdata/apollo/gc.log -Denv=DEV -Ddev_meta=http://127.0.0.1:8080  -jar springcloud-config-apollo-1.0.0.jar >/dev/null 2>&1 &
