package com.dingmk.location.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.google.common.util.concurrent.RateLimiter;

/**
 * 全局公用配置
 * 
 * @author Tony.Lau
 * @createTime 2017-10-30
 */
@Configuration
public class GlobalConfigService{

	@Value("${phone.thirdparty.query.globalLimit:300}")
    private int globalLimit = 300;
	private RateLimiter globalLimiter = RateLimiter.create(globalLimit);

    @Value("${phone.areacode.gaode.Limit:20}")
    private int gaodeLimit = 20;
    private RateLimiter gaodeLimiter = RateLimiter.create(gaodeLimit);

    @Value("${phone.areacode.baidu.Limit:20}")
    private int baiduLimit = 20;
    private RateLimiter baiduLimiter = RateLimiter.create(baiduLimit);

    @Value("${phone.info.mongo.backSource.enable:false}")
    private boolean mongoBackSourceEnable;
    
    @Value("${phone.info.mongo.backSource.limit:1000}")
    private int mongoBackSourceLimit = 1000;
    private RateLimiter mongoBackSourceLimiter = RateLimiter.create(mongoBackSourceLimit);
    
    @Value("${phone.thirdparty.query.sleepTime:150}")
    private long sleepTime;

    @Value("${phone.thirdparty.query.switchMode:SYNC}")
    private SwitchMode switchMode;

    @Value("${phone.collect.query.mongoEnable:true}")
    private boolean mongoEnable;

    @Value("${com.onlineteam.pageSize:10000}")
    private int pageSize = 10000;

    public RateLimiter getGlobalLimiter() {
        return this.globalLimiter;
    }

    public RateLimiter getGaodeLimiter() {
        return this.gaodeLimiter;
    }

    public RateLimiter getBaiduLimiter() {
        return this.baiduLimiter;
    }
    
    public boolean isMongoBackSourceEnable(){
        return this.mongoBackSourceEnable;
    }
    
    public RateLimiter getMongoBackSourceLimiter(){
        return this.mongoBackSourceLimiter;
    }

    public long getSleepTime() {
        return this.sleepTime;
    }

    public SwitchMode getSwitchMode() {
        return this.switchMode;
    }

    public boolean isMongoEnable() {
        return this.mongoEnable;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public enum SwitchMode {
        ASYNC, SYNC;
    }

}
