package com.dingmk.zipkin.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ZipkinCLientConfig {
	
	@Value("${zipkin.client.threadpool.size:2}")
	private int threadPoolCounts;

	@Autowired
    BeanFactory beanFactory;
	
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public ExecutorService executorService(){
        ExecutorService executorService =  Executors.newFixedThreadPool(threadPoolCounts);
        return new TraceableExecutorService(this.beanFactory, executorService);
    }
}
