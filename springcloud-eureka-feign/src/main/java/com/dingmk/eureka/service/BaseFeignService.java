package com.dingmk.eureka.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dingmk.eureka.config.BaseRequest;
import com.dingmk.eureka.config.BaseResponse;
import com.dingmk.eureka.feign.BaseServiceFeign;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BaseFeignService {
	
	@Resource
	private BaseServiceFeign baseFeign;

	public BaseResponse query(BaseRequest request) {
		BaseResponse response = baseFeign.queryLocation(request);
		
		if (log.isDebugEnabled()) {
			log.debug("query base feign response: {}", response.toString());
		}
		return response;
	}
	
}
