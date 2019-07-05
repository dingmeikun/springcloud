package com.dingmk.eureka.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dingmk.eureka.dao.BaseDao;
import com.dingmk.eureka.model.BaseRequest;
import com.dingmk.eureka.model.BaseRequest.Location;
import com.dingmk.eureka.model.BaseResponse;
import com.dingmk.eureka.model.BaseResponse.Address;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BaseService {
	
	@Resource
    private BaseDao dao;

	public BaseResponse query(BaseRequest request) {
		if(log.isDebugEnabled()) {
			log.debug("recieve ip is [{}]", request.getIp());
		}
		
		Location location = request.getLocation();
		Address address = dao.getAddress(location);
		
		BaseResponse response = new BaseResponse();
		response.setCoordsys("net");
		response.setLocation(location.getLng() + "" + location.getLat());
		response.setAddress(address);
		
		return response;
	}
}
