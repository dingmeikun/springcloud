package com.dingmk.config.configure;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ZuulPropertiesRefresher implements ApplicationContextAware{

	@Autowired
	private RouteLocator routeLocator;
	
	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@ApolloConfigChangeListener(value = "TEST1.zuul_config")
	public void onChange(ConfigChangeEvent changeEvent) {
		boolean zuulPropertiesChanged = false;
		for (String changeKey : changeEvent.changedKeys()) {
			if (changeKey.startsWith("zuul.")) {
				zuulPropertiesChanged = true;
				break;
			}
		}
		
		if (zuulPropertiesChanged) {
			refreshZuulProperties(changeEvent);
		}
	}
	
	private void refreshZuulProperties(ConfigChangeEvent changeEvent) {
		log.info("Refresh zuul properties begined!");
		
		this.applicationContext.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));
		
		this.applicationContext.publishEvent(new RoutesRefreshedEvent(routeLocator));
		
		log.info("Refresh zuul properties finished!");
	}
	
}
