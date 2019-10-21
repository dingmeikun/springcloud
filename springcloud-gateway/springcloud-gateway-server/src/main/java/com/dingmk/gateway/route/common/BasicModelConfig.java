package com.dingmk.gateway.route.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import com.dingmk.gateway.route.utils.IOUtil;
import com.google.common.util.concurrent.RateLimiter;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 全局公用配置
 * 
 * @author Tony.Lau
 * @createTime 2019-03-30
 */
@Slf4j
@Getter
@Setter
@Service
@DisconfFile(filename = "gateway.properties")
@DisconfUpdateService(confFileKeys = { "gateway.properties" })
public class BasicModelConfig implements IDisconfUpdate {
	
	private static final ResourceLoader resourceLoader = new DefaultResourceLoader();
	
	private int globalLimit = 300;
    private RateLimiter globalLimiter = RateLimiter.create(globalLimit);
    
    private int cacheExpire = 60;
    
    private int cacheCapacity = 10;
    
    private String cacheFreshCorn = "0 0/10 * * * ?";
	
    @PostConstruct
    public void postConstruct() {
        reload();
    }
    
	@Override
	public void reload() {
		Properties props = new Properties();
        InputStream is = null;
        String location = "gateway.properties";
        try {
            Resource resource = resourceLoader.getResource(location);
            is = resource.getInputStream();
            props.load(is);
            if (props.isEmpty()) {
                log.warn("Could not load properties from path:{}, {} ", location);
                return;
            }
        } catch (IOException e) {
            log.error("Could not load properties from path:{}, {} ", location, e.getMessage(), e);
        } finally {
            IOUtil.closeQuietly(is);
        }
        
        String globalLimitTmp = props.getProperty("gateway.mysql.query.limit");
        
        String cacheExpireTmp = props.getProperty("gateway.local.cache.expire");
        String cacheCapacityTmp = props.getProperty("gateway.local.cache.capacity");
        String cacheFreshCornTmp = props.getProperty("gateway.local.cache.freshCorn");

        if (null != globalLimitTmp && !(globalLimitTmp = globalLimitTmp.trim()).isEmpty()) {
            globalLimit = Integer.parseInt(globalLimitTmp);
            globalLimiter = RateLimiter.create(globalLimit);
        }
        
        if (null != cacheExpireTmp && !(cacheExpireTmp = cacheExpireTmp.trim()).isEmpty()) {
        	cacheExpire = Integer.parseInt(cacheExpireTmp);
        }
        
        if (null != cacheCapacityTmp && !(cacheCapacityTmp = cacheCapacityTmp.trim()).isEmpty()) {
        	cacheCapacity = Integer.parseInt(cacheCapacityTmp);
        }
        
        if (null != cacheFreshCornTmp && !(cacheFreshCornTmp = cacheFreshCornTmp.trim()).isEmpty()) {
        	cacheFreshCorn = cacheFreshCornTmp;
        }
	}

}
