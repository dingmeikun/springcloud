package com.dingmk.gateway.route.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
//@DisconfFile(filename = "route.properties")
//@DisconfUpdateService(confFileKeys = { "route.properties" })
//@Configuration
public class DynamicRouteConfig implements IDisconfUpdate {

	private ApplicationEventPublisher publisher;
	private static final ResourceLoader resourceLoader = new DefaultResourceLoader();
	private Properties props = null;
	
	@PostConstruct
    public void postConstruct() {
        reload();
    }

    @Override
    public void reload() {
        Properties props = new Properties();
        InputStream is = null;
        String location = "route.properties";
        try {
            Resource resource = resourceLoader.getResource(location);
            is = resource.getInputStream();
            props.load(is);
            if (props.isEmpty()) {
                log.warn("Could not load properties from path:{}, {} ", location);
                return;
            }
            
            Map<String, RouteDefinition> tempMap = Maps.newHashMap();
            Enumeration<Object> keys = props.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                String value = props.getProperty(key);
                if (StringUtils.isNotEmpty(value)) {
                	RouteDefinition routeDef = JSONObject.parseObject(value, RouteDefinition.class);
                    if (null != routeDef) {
                        tempMap.put(key, routeDef);
                    }
                }
            }
            DynamicRouteCache.storeRoutes.clear();
            DynamicRouteCache.storeRoutes.putAll(tempMap);
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            log.info("LOAD ROUTES RULE END,SIZE={}", tempMap.size());
        } catch (IOException e) {
            log.error("Could not load properties from path:{}, {} ", location, e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
    
    public String getValue(String key) {
        return props.getProperty(key);
    }

    public String getValue(String key, String defalutVal) {
        return props.getProperty(key, defalutVal);
    }
}
