package com.dingmk.zuul.service;

import org.springframework.stereotype.Service;

import com.dingmk.zuul.config.BaseRequest;
import com.dingmk.zuul.config.BaseRequest.Location;
import com.dingmk.zuul.config.BaseResponse;
import com.dingmk.zuul.config.BaseResponse.Address;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BaseService {
	
	public BaseResponse query(BaseRequest request) {
		if(log.isDebugEnabled()) {
			log.debug("recieve ip is [{}]", request.getIp());
		}
		
		Location location = request.getLocation();
		Address address = new Address();
		
		address.setCountry("中国");
		address.setProvince("江西省");
		address.setCity("赣州市");
		address.setLocation(location.getLng() + ", " + location.getLat());
		address.setStreet("红旗大道");
		address.setFmtAddress("中国 江西省赣州市章贡区红旗大道江西理工大学");
		
		BaseResponse response = new BaseResponse();
		response.setCoordsys("springcloud zuul simple node!");
		response.setLocation(location.getLng() + ", " + location.getLat());
		response.setAddress(address);
		
		return response;
	}
}
