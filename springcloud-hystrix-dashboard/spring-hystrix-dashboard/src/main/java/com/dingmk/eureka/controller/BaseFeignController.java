package com.dingmk.eureka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dingmk.eureka.config.BaseRequest;
import com.dingmk.eureka.config.BaseResponse;
import com.dingmk.eureka.service.BaseFeignService;

import lombok.extern.slf4j.Slf4j;

/**
 * 此为业务项目入口，controller可由自己框架定制，也可使用springmcv映射url访问
 * @author dingmeikun
 *
 */
@Slf4j
@RestController
public class BaseFeignController {
	
	@Autowired
	private BaseFeignService service;
	
	@PostMapping("/data/feign/query/v1")
    public BaseResponse queryForList(@RequestBody BaseRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("请求参数：{}", JSONObject.toJSONString(request));
        }

        BaseResponse result = service.query(request);

        if (log.isDebugEnabled()) {
            log.debug("响应参数：{}", JSONObject.toJSONString(result));
        }

        return result;
    }
	
}
