package com.dingmk.onlineteam.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import com.dingmk.onlineteam.model.StatisticsPointMessage;
import com.dingmk.onlineteam.util.GlobalConfigService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class StatisticsController {
	
	@Resource
	private GlobalConfigService service;

    protected void doService(StatisticsPointMessage statisticsPointMessage) {
    	if (service.getGaodeLimiter().tryAcquire()) {
    		log.debug("service tryAcquire!");
    	}
    }

}
