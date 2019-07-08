package com.dingmk.location.controller;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dingmk.location.dto.LocationRequest;
import com.dingmk.location.dto.LocationRequest.Location;
import com.dingmk.location.dto.LocationResponse;
import com.dingmk.location.service.LocationService;
import com.dingmk.location.util.LocationUtil;

/**
 * Location 服务Controller
 * @author dingmk
 *
 */
@Slf4j
@RestController
public class LocationController {
	
	@Resource
	LocationService service;
	
	/**
	 * 业务逻辑：
	 * 	1.按照location查询MongoDB地理位置信息，如果有：直接返回   如果没有：执行2
	 *  2.按照location信息查询高德/百度API获取位置信息，执行3
	 *  3.将消息入库，并且响应消费端
	 *  
	 * @param request 经纬度请求信息
	 * @return 地理位置信息
	 */
	@RequestMapping("/location/geo/query/v1")
	public LocationResponse queryLocation(@RequestBody LocationRequest request){
		if (log.isDebugEnabled()) {
            log.debug("请求参数：{}", JSONObject.toJSONString(request));
        }
		
		LocationResponse response = service.query(request);
		
		if (log.isDebugEnabled()) {
            log.debug("响应参数：{}", JSONObject.toJSONString(response));
        }
		
		return compareResponse(request, response);
	}
	
	/**
	 * 处理响应数据
	 * 
	 * @param request
	 * @param result
	 * @return
	 */
	private LocationResponse compareResponse(LocationRequest request, LocationResponse result){
		LocationResponse response = new LocationResponse();
		Location location = request.getLocation();
		if(null != result && null != result.getAddress() && null != result.getLocation()){
			return result;
		}
		
		response.setCoordsys(location.getCoordsys());
		response.setLocation(LocationUtil.getLocationStr(location));
		return response;
	}
}
