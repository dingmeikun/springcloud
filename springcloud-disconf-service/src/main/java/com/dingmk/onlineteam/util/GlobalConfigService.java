package com.dingmk.onlineteam.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import com.google.common.util.concurrent.RateLimiter;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局公用配置
 * 
 * @author Tony.Lau
 * @createTime 2017-10-30
 */
@Slf4j
@Service
@DisconfFile(filename = "globalconfig.properties")
@DisconfUpdateService(confFileKeys = { "globalconfig.properties" })
public class GlobalConfigService implements IDisconfUpdate {

    private static final ResourceLoader resourceLoader = new DefaultResourceLoader();

    private int globalLimit = 300;
    private RateLimiter globalLimiter = RateLimiter.create(globalLimit);

    private int gaodeLimit = 20;
    private RateLimiter gaodeLimiter = RateLimiter.create(gaodeLimit);

    private int baiduLimit = 20;
    private RateLimiter baiduLimiter = RateLimiter.create(baiduLimit);

    private boolean mongoBackSourceEnable = false;
    private int mongoBackSourceLimit = 1000;
    private RateLimiter mongoBackSourceLimiter = RateLimiter.create(mongoBackSourceLimit);
    

    private long sleepTime = 150;

    private SwitchMode switchMode = SwitchMode.SYNC;

    private boolean mongoEnable = true;

    private int pageSize = 10000;

    public RateLimiter getGlobalLimiter() {
        return globalLimiter;
    }

    public RateLimiter getGaodeLimiter() {
        return gaodeLimiter;
    }

    public RateLimiter getBaiduLimiter() {
        return baiduLimiter;
    }
    
    public boolean isMongoBackSourceEnable(){
        return mongoBackSourceEnable;
    }
    
    public RateLimiter getMongoBackSourceLimiter(){
        return mongoBackSourceLimiter;
    }

    public long getSleepTime() {
        return sleepTime;
    }

    public SwitchMode getSwitchMode() {
        return switchMode;
    }

    public boolean isMongoEnable() {
        return mongoEnable;
    }

    public int getPageSize() {
        return pageSize;
    }

    public enum SwitchMode {
        ASYNC, SYNC;
    }

    @PostConstruct
    public void postConstruct() {
        reload();
    }

    @Override
    public void reload() {
        Properties props = new Properties();
        InputStream is = null;
        String location = "globalconfig.properties";
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
            IOUtils.closeQuietly(is);
        }

        String globalLimitTmp = props.getProperty("phone.thirdparty.query.globalLimit");

        String gaodeLimitTmp = props.getProperty("phone.areacode.gaode.Limit");
        String baiduLimitTmp = props.getProperty("phone.areacode.baidu.Limit");

        String mongoBackSourceEnableTmp = props.getProperty("phone.info.mongo.backSource.enable");
        String mongoBackSourceLimitTmp = props.getProperty("phone.info.mongo.backSource.limit");

        String sleepTimeTmp = props.getProperty("phone.thirdparty.query.sleepTime");
        String switchModeTmp = props.getProperty("phone.thirdparty.query.switchMode");
        String mongoEnableTmp = props.getProperty("phone.collect.query.mongoEnable");
        String pageSizeTmp = props.getProperty("com.onlineteam.pageSize");

        if (null != globalLimitTmp && !(globalLimitTmp = globalLimitTmp.trim()).isEmpty()) {
            globalLimit = Integer.parseInt(globalLimitTmp);
            globalLimiter = RateLimiter.create(globalLimit);
        }

        if (null != gaodeLimitTmp && !(gaodeLimitTmp = gaodeLimitTmp.trim()).isEmpty()) {
            gaodeLimit = Integer.parseInt(gaodeLimitTmp);
            gaodeLimiter = RateLimiter.create(gaodeLimit);
        }

        if (null != baiduLimitTmp && !(baiduLimitTmp = baiduLimitTmp.trim()).isEmpty()) {
            baiduLimit = Integer.parseInt(baiduLimitTmp);
            baiduLimiter = RateLimiter.create(baiduLimit);
        }

        if (null != mongoBackSourceEnableTmp
                && !(mongoBackSourceEnableTmp = mongoBackSourceEnableTmp.trim()).isEmpty()) {
            mongoBackSourceEnable = Boolean.parseBoolean(mongoBackSourceEnableTmp);
        }

        if (null != mongoBackSourceLimitTmp && !(mongoBackSourceLimitTmp = mongoBackSourceLimitTmp.trim()).isEmpty()) {
            mongoBackSourceLimit = Integer.parseInt(mongoBackSourceLimitTmp);
            mongoBackSourceLimiter = RateLimiter.create(mongoBackSourceLimit);
        }

        if (null != sleepTimeTmp && !(sleepTimeTmp = sleepTimeTmp.trim()).isEmpty()) {
            sleepTime = Integer.parseInt(sleepTimeTmp);
        }

        if (null != switchModeTmp && !(switchModeTmp = switchModeTmp.trim()).isEmpty()) {
            switchMode = SwitchMode.valueOf(switchModeTmp.toUpperCase());
        }

        if (null != mongoEnableTmp && !(mongoEnableTmp = mongoEnableTmp.trim()).isEmpty()) {
            mongoEnable = Boolean.parseBoolean(mongoEnableTmp);
        }

        if (null != pageSizeTmp && !(pageSizeTmp = pageSizeTmp.trim()).isEmpty()) {
            pageSize = Integer.parseInt(pageSizeTmp);
        }
    }

}
