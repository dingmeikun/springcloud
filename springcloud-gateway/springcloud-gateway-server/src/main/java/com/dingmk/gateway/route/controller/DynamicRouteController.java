package com.dingmk.gateway.route.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dingmk.gateway.route.common.BasicLogicResult;
import com.dingmk.gateway.route.model.CustomRouteDefinition;
import com.dingmk.gateway.route.support.DynamicRouteService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/gateway")
public class DynamicRouteController {
	
	@Autowired
    private DynamicRouteService dynamicRouteService;
	
	/**
     * 增加路由
     * @param
     * @return
     */
    @PostMapping("/route/add")
    public BasicLogicResult<String> add(@RequestBody CustomRouteDefinition request) {
    	if (log.isDebugEnabled()) {
    		log.debug("Gateway[ADD] 请求参数：{}", JSONObject.toJSONString(request));
    	}
    	
    	BasicLogicResult<String> response = dynamicRouteService.add(request);
    	
    	if (log.isDebugEnabled()) {
            log.debug("Gateway[ADD] 响应参数：{}", JSONObject.toJSONString(response));
        }
    	return response;
    }
    
    /**
     * 修改路由
     * @param definition
     * @return
     */
    @PostMapping("/route/update")
    public BasicLogicResult<String> update(@RequestBody CustomRouteDefinition request) {
    	if (log.isDebugEnabled()) {
    		log.debug("Gateway[UPDATE] 请求参数：{}", JSONObject.toJSONString(request));
    	}
    	
    	BasicLogicResult<String> response = dynamicRouteService.update(request);
        
        if (log.isDebugEnabled()) {
            log.debug("Gateway[UPDATE] 响应参数：{}", JSONObject.toJSONString(response));
        }
        return response;
    }

    /**
     * 删除路由
     * @param RouteId
     * @return
     */
    @DeleteMapping("/route/delete/{routeId}")
    public BasicLogicResult<String> delete(@PathVariable("routeId") String routeId) {
    	if (log.isDebugEnabled()) {
    		log.debug("Gateway[DELETE] 请求参数：{}", routeId);
    	}
    	
    	BasicLogicResult<String> response = dynamicRouteService.delete(routeId);
        
    	if (log.isDebugEnabled()) {
            log.debug("Gateway[DELETE] 响应参数：{}", JSONObject.toJSONString(response));
        }
        return response;
    }
    
    /**
     * 同步路由（db ->redis）
     * @return
     */
    @GetMapping("/route/sync")
    public BasicLogicResult<String> syncRoutes() {
    	BasicLogicResult<String> response = dynamicRouteService.synchronization();
    	
    	if (log.isDebugEnabled()) {
            log.debug("Gateway[SYNC] 响应参数：{}", JSONObject.toJSONString(response));
        }
    	return response;
    }
    
    /**
     * 上线路由
     * @param
     * @return
     */
    @GetMapping("/route/online")
    public BasicLogicResult<String> onlineRoute(@RequestParam("routeId") String routeId, @RequestParam("status") int status) {
    	if (log.isDebugEnabled()) {
    		log.debug("Gateway[ONLINE] 请求参数：routeId [{}], status [{}]", routeId, status);
    	}
    	
    	BasicLogicResult<String> response = dynamicRouteService.online(routeId, status);
    	
    	if (log.isDebugEnabled()) {
            log.debug("Gateway[ONLINE] 响应参数：{}", JSONObject.toJSONString(response));
        }
    	return response;
    }
    
    /**
     * 下线路由
     * @param
     * @return
     */
    @GetMapping("/route/offline")
    public BasicLogicResult<String> offlineRoute(@RequestParam("routeId") String routeId, @RequestParam("status") int status) {
    	if (log.isDebugEnabled()) {
    		log.debug("Gateway[OFFLINE] 请求参数：routeId [{}], status [{}]", routeId, status);
    	}
    	
    	BasicLogicResult<String> response = dynamicRouteService.offline(routeId, status);
    	
    	if (log.isDebugEnabled()) {
            log.debug("Gateway[OFFLINE] 响应参数：{}", JSONObject.toJSONString(response));
        }
    	return response;
    }
}
