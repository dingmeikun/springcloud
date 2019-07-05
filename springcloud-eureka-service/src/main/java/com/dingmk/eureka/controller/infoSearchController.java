package com.dingmk.eureka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dingmk.eureka.model.BaseRequest;
import com.dingmk.eureka.model.BaseResponse;
import com.dingmk.eureka.service.BaseService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class infoSearchController {
	
	@Autowired
	private BaseService service;

	@PostMapping("/data/official/query/v1")
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
