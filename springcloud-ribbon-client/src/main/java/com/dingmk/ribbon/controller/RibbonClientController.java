package com.dingmk.ribbon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dingmk.ribbon.model.BaseRequest;
import com.dingmk.ribbon.model.BaseResponse;
import com.dingmk.ribbon.service.RibbonService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class RibbonClientController {
	
	@Autowired
	private RibbonService service;

	@PostMapping("/data/ribbon/query/v1")
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
