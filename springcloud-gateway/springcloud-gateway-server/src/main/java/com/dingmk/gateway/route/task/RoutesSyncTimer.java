package com.dingmk.gateway.route.task;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dingmk.gateway.route.support.DynamicRouteSyncService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Configuration
public class RoutesSyncTimer {
	
	private static volatile boolean run = false;
	
	@Resource
	private DynamicRouteSyncService syncService;
	
	@Scheduled(cron = "${gateway.local.cache.freshCorn}")
	public void start() {
		try {
	      if (run) {
	          log.warn("Routes sync is running...");
	          return;
	      }
	      run = true;
	      
	      syncService.syncRedis();
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