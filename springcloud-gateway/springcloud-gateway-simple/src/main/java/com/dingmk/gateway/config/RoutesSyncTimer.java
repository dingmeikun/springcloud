package com.dingmk.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Configuration
public class RoutesSyncTimer {
	
	private static volatile boolean run = false;
	
	@Value("${num.cron}")
	private String period;
	
//	@Scheduled(fixedDelayString="${sync.data.cache.period:60000}")
	@Scheduled(cron = "${num.cron}")
	public void start() {
		System.err.println("+=====> period:" + period);
		try {
	      if (run) {
	          log.warn("Routes sync is running...");
	          return;
	      }
	      run = true;
	      
		} catch (Exception e) {
			log.error("ROUTE_SYNC_MEET_EXCEPTION:" + e.getMessage(), e);
			return;
		} finally {
			run = false;
		}
	}
	
	@Scheduled(fixedDelay = 60000)
	public void alive() {
		log.info("ALIVE");
	}
}