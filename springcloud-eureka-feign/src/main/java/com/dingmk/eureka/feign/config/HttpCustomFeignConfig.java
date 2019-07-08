package com.dingmk.eureka.feign.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class HttpCustomFeignConfig {
	
	/** Idel线程清理回调方法 */
	private IdleConnectionMonitorThread releaser;
	
	/** Idel线程清理线程池 */
	private ScheduledExecutorService taskService;
	
	/** HTTP最大连接数 */
	@Value("${feign.httpclient.connpool.maxTotalPoolConn:2048}")
	private int maxTotalPoolConn;
	
	/** HTTP同路由的并发数 */
	@Value("${feign.httpclient.connpool.maxPerRoute:1024}")
	private int maxPerRoute;
	
	/** HTTP连接池获取连接超时时间 */
	@Value("${feign.httpclient.connpool.connectionRequestTimeout:1500}")
	private int connectionRequestTimeout;
	
	/** HTTP链接建立时间 */
	@Value("${feign.httpclient.connpool.connectTimeout:1500}")
	private int connectTimeout;
	
	/** HTTP等待数据包超时时间 */
	@Value("${feign.httpclient.connpool.socketTimeout:2000}")
	private int socketTimeout;
	
	/** HTTP连接空闲时间 */
	@Value("${feign.httpclient.connpool.idleConnTimeout:20000}")
	private int idleConnTimeout;
	
	/** HTTP清除过期链接时间间隔 */
	@Value("${feign.httpclient.connpool.connectionTimerRepeat:3000}")
	private int connectionTimerRepeat;
	
	/** HTTP请求重试次数 */
	@Value("${feign.httpclient.connpool.httpRequestRetryLimit:0}")
	private int httpRequestRetryLimit;
	
	/** HTTP请求重试许可 */
	@Value("${feign.httpclient.connpool.httpRequestRetryEnable:false}")
	private boolean httpRequestRetryEnable;
	
	@Bean
	public HttpClient httpClient(HttpClientBuilder httpClientBuilder) {
		return httpClientBuilder.build();
	}
	
	@Bean
	public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		try {
			SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(socketTimeout).build();
			connectionManager.setMaxTotal(maxTotalPoolConn);
			connectionManager.setDefaultMaxPerRoute(maxPerRoute);
			connectionManager.setDefaultSocketConfig(socketConfig);
		
			releaser = new IdleConnectionMonitorThread(connectionManager, idleConnTimeout);
			if(null != taskService) {
				taskService.shutdown();
			}
			taskService = Executors.newSingleThreadScheduledExecutor();
			taskService.scheduleAtFixedRate(releaser, 0, connectionTimerRepeat, TimeUnit.SECONDS);
		} catch (Exception e) {
			log.error("Custom PoolingHttpClientConnectionManager init error! message: {}", e.getMessage(), e);
		}
		
		return connectionManager;
	}
	
	@Bean
	public HttpClientBuilder httpClientBuilder(HttpClientConnectionManager connectionManager) {
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(connectTimeout)
				.setSocketTimeout(socketTimeout)
				.setConnectionRequestTimeout(connectionRequestTimeout)
				.build();
		
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		builder.setDefaultRequestConfig(requestConfig);
		builder.setRetryHandler(new DefaultHttpRequestRetryHandler(httpRequestRetryLimit, httpRequestRetryEnable));
		builder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
		return builder;
	}
	
	private class IdleConnectionMonitorThread implements Runnable {
		private final int idleTimeout;
		private final HttpClientConnectionManager poolConnManager;

		public IdleConnectionMonitorThread(HttpClientConnectionManager poolConnManager, int idleTimeout) {
			this.poolConnManager = poolConnManager;
			this.idleTimeout = idleTimeout;
		}

		@Override
		public void run() {
			poolConnManager.closeExpiredConnections();
			poolConnManager.closeIdleConnections(idleTimeout, TimeUnit.MILLISECONDS);
		}

	}
}
