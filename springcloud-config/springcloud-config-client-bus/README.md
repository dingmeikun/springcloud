### manual refresh config
此为手动生效的config刷新设置

#### 操作步骤
(1)启动config-server

(2)启动config-client-manualrefresh

(3)测试访问结果
查看config-server是否生效，可访问拿到config-server中指向git中的文件

	http://192.168.101.15:8014/config-info/test/master
	
通过config-client拿到config-server中的数据

	http://192.168.101.15:8015/getconfigInfo

修改git中文件数据，在进行查看

	http://192.168.101.15:8015/getconfigInfo