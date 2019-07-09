package com.dingmk.ribbon.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.dingmk.ribbon.model.BaseRequest;
import com.dingmk.ribbon.model.BaseResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RibbonService {
	
	@Autowired
    private RestTemplate restTemplate;

	public BaseResponse query(BaseRequest request) {
		if(log.isDebugEnabled()) {
			log.debug("recieve ip is [{}]", request.getIp());
		}
		
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();
		
		param.add("ip", request.getIp());
		param.add("location", request.getLocation());
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(param, headers);
		
		try {
			URI uri = new URI("http://eureka-service/data/service/query/v1");
			
			//带请求头信息
			return restTemplate.postForObject(uri, entity, BaseResponse.class);
			
			//不带头信息
			//return restTemplate.postForObject("http://eureka-service/data/service/query/v1", param, BaseResponse.class);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
