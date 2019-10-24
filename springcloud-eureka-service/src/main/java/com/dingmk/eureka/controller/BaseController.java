package com.dingmk.eureka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dingmk.eureka.model.BaseRequest;
import com.dingmk.eureka.model.BaseResponse;
import com.dingmk.eureka.model.BaseRequest.Location;
import com.dingmk.eureka.model.BaseResponse.Address;
import com.dingmk.eureka.service.BaseService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class BaseController {
	
	@Autowired
	private BaseService service;

	@HystrixCommand(fallbackMethod = "bizFallback")
	@PostMapping("/data/service/query/v1")
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
	
	public BaseResponse bizFallback(@RequestBody BaseRequest request) {
		Location location = request.getLocation();
		Address address = new Address();
		
		BaseResponse response = new BaseResponse();
		response.setCoordsys("Hystrix Fallback");
		response.setLocation(location.getLng() + ", " + location.getLat());
		response.setAddress(address);
		
		return response;
    }
}
