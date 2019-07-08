package com.dingmk.eureka.hystrix;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.dingmk.eureka.config.BaseRequest;
import com.dingmk.eureka.config.BaseRequest.Location;
import com.dingmk.eureka.config.BaseResponse;
import com.dingmk.eureka.feign.BaseServiceFeign;

@Component
public class BaseHystrix implements BaseServiceFeign{

	@Override
    public BaseResponse queryLocation(@RequestBody BaseRequest location) {
    	Location loc = location.getLocation();
    	BaseResponse response = new BaseResponse();
    	response.setAddress(null);
    	response.setLocation(loc.getLng() + ", " + loc.getLat());
    	response.setCoordsys(loc.getCoordsys());
    	return response;
    }
}
