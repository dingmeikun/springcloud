package com.dingmk.gateway.route.utils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.alibaba.fastjson.JSONObject;
import com.dingmk.comm.utils.JsonMapper;
import com.dingmk.gateway.route.dto.CustomGatewayRoutes;
import com.dingmk.gateway.route.model.CustomFilterDefinition;
import com.dingmk.gateway.route.model.CustomPredicateDefinition;
import com.dingmk.gateway.route.model.CustomRouteDefinition;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtil {
	
	/**
	 *  转化路由对象  （RouteDefinition -> redis）
	 *  RouteDefinition -> CustomRouteDefinition
	 *  
	 * @param redisGatewayRoutes
	 * @return
	 */
	public static CustomRouteDefinition convertToCustomRouteDefinition(RouteDefinition routeDefinition) {
		CustomRouteDefinition redisRoutes = new CustomRouteDefinition();
		if (null != routeDefinition &&  null != routeDefinition.getId()) {
			redisRoutes.setId(routeDefinition.getId());
			redisRoutes.setOrder(routeDefinition.getOrder());
			redisRoutes.setUri(routeDefinition.getUri().toString());
			
			List<FilterDefinition> filters = routeDefinition.getFilters();
			if (!CollectionUtils.isEmpty(filters)) {
				try {
					String filterJson = JsonMapper.nonDefaultMapper().toJson(filters);
					List<CustomFilterDefinition> filter = JSONObject.parseArray(filterJson, CustomFilterDefinition.class);
					redisRoutes.setFilters(filter);
				} catch (Exception e) {
					log.error("Parse CustomFilterDefinition meet error! [{}]", e.getMessage(), e);
				}
			}
			
			List<PredicateDefinition> predicates = routeDefinition.getPredicates();
			if (!CollectionUtils.isEmpty(predicates)) {
				try {
					String predicateJson = JsonMapper.nonDefaultMapper().toJson(predicates);
					List<CustomPredicateDefinition> predicate = JSONObject.parseArray(predicateJson, CustomPredicateDefinition.class);
					redisRoutes.setPredicates(predicate);
				} catch (Exception e) {
					log.error("Parse CustomPredicateDefinition meet error! [{}]", e.getMessage(), e);
				}
			}
			
			return redisRoutes;
		}
		return null;
	}
	
	/**
	 *  转化路由对象  （db -> redis）
	 *  CustomGatewayRoutes -> CustomRouteDefinition
	 *  
	 * @param redisGatewayRoutes
	 * @return
	 */
	public static CustomRouteDefinition convertToCustomRouteDefinition(CustomGatewayRoutes customGatewayRoutes) {
		CustomRouteDefinition customRouteDefinition = new CustomRouteDefinition();
		if (null != customGatewayRoutes && null != customGatewayRoutes.getRouteId() && null != customGatewayRoutes.getUri()) {
			customRouteDefinition.setId(customGatewayRoutes.getRouteId());
			customRouteDefinition.setUri(customGatewayRoutes.getUri());
			
			customRouteDefinition.setOrder(customGatewayRoutes.getOrders());
			customRouteDefinition.setDescription(customGatewayRoutes.getDescription());
			
			String filter = customGatewayRoutes.getFilters();
			if (null != filter && !filter.trim().isEmpty()) {
				try {
					List<CustomFilterDefinition> filters = JSONObject.parseArray(filter, CustomFilterDefinition.class);
					customRouteDefinition.setFilters(filters);
				} catch (Exception e) {
					log.error("Parse CustomFilterDefinition meet error! [{}]", e.getMessage(), e);
				}
			}
			
			String predicate = customGatewayRoutes.getPredicates();
			if (null != predicate && !predicate.trim().isEmpty()) {
				try {
					List<CustomPredicateDefinition> predicates = JSONObject.parseArray(predicate, CustomPredicateDefinition.class);
					customRouteDefinition.setPredicates(predicates);
				} catch (Exception e) {
					log.error("Parse CustomPredicateDefinition meet error! [{}]", e.getMessage(), e);
				}
			}
			
			return customRouteDefinition;
		}
		return null;
	}
	
	/**
     *	 转化路由对象  （redis -> db）
     *	CustomRouteDefinition -> CustomGatewayRoutes
     *
     * @param gatewayRouteDefinition
     * @return
     */
    public static CustomGatewayRoutes convertToCustomDefinition(CustomRouteDefinition customRouteDefinition){
    	CustomGatewayRoutes customGatewayRoutes = new CustomGatewayRoutes();
    	if (null != customRouteDefinition && null != customRouteDefinition.getId() && null != customRouteDefinition.getUri()) {
    		customGatewayRoutes.setRouteId(customRouteDefinition.getId());
    		customGatewayRoutes.setUri(customRouteDefinition.getUri());
    		
    		customGatewayRoutes.setOrders(customRouteDefinition.getOrder());
    		customGatewayRoutes.setDescription(customRouteDefinition.getDescription());
    		
    		List<CustomFilterDefinition> filter = customRouteDefinition.getFilters();
    		if (!CollectionUtils.isEmpty(filter)) {
    			String filters = JsonMapper.nonDefaultMapper().toJson(filter);
    			customGatewayRoutes.setFilters(filters);
    		}
    		
    		List<CustomPredicateDefinition> predicate = customRouteDefinition.getPredicates();
    		if (!CollectionUtils.isEmpty(predicate)) {
    			String predicates = JsonMapper.nonDefaultMapper().toJson(predicate);
    			customGatewayRoutes.setPredicates(predicates);
    		}
    		
    		return customGatewayRoutes;
    	}
        
        return null;
    }
	
    /**
     * 转化对象 （redis -> definition）
     * CustomRouteDefinition -> RouteDefinition
     * 
     * @param customRoutes
     * @return
     */
	public static RouteDefinition convertToRouteDefinition(CustomRouteDefinition customRoutes) {
		if (null != customRoutes && null != customRoutes.getId()) {
			RouteDefinition definition = new RouteDefinition();
			
			// ID
			definition.setId(customRoutes.getId());
			
			// URI
			String uriStr = customRoutes.getUri();
			if (null != uriStr && !uriStr.trim().isEmpty()) {
				URI uri = UriComponentsBuilder.fromUriString(uriStr).build().toUri();
    			definition.setUri(uri);
			}
			
			// Order
			definition.setOrder(customRoutes.getOrder());
			
			// Predicate
			List<CustomPredicateDefinition> predicates = customRoutes.getPredicates();
			if (!CollectionUtils.isEmpty(predicates)) {
				try {
					String predicateJson = JsonMapper.nonDefaultMapper().toJson(predicates);
					List<PredicateDefinition> predicate = JSONObject.parseArray(predicateJson, PredicateDefinition.class);
					definition.setPredicates(predicate);
				} catch (Exception e) {
					log.error("Parse PredicateDefinition meet error! [{}]", e.getMessage(), e);
				}
			}

			// Filter
			List<CustomFilterDefinition> filters = customRoutes.getFilters();
			if (!CollectionUtils.isEmpty(filters)) {
				try {
					String filterJson = JsonMapper.nonDefaultMapper().toJson(filters);
					List<FilterDefinition> filterDef = JSONObject.parseArray(filterJson, FilterDefinition.class);
					definition.setFilters(filterDef);
				} catch (Exception e) {
					log.error("Parse FilterDefinition meet error! [{}]", e.getMessage(), e);
				}
			}
			
			return definition;
		}
		
		return null;
	}

	/**
     * 转化对象（db -> definition）
     * CustomGatewayRoutes -> RouteDefinition
     * 
     * @param customRoutes
     * @return
     */
	public static RouteDefinition convertToRouteDefinition(CustomGatewayRoutes customRoutes) {
		if (null != customRoutes && null != customRoutes.getRouteId()) {
			RouteDefinition definition = new RouteDefinition();
			
			// ID
			definition.setId(customRoutes.getRouteId());
			
			// URI
			String uriStr = customRoutes.getUri();
			if (null != uriStr && !uriStr.trim().isEmpty()) {
				URI uri = UriComponentsBuilder.fromUriString(uriStr).build().toUri();
    			definition.setUri(uri);
			}
			
			// Order
			definition.setOrder(customRoutes.getOrders());
			
			// Predicate
			String predicate = customRoutes.getPredicates();
			if (null != predicate && !predicate.isEmpty()) {
				try {
					List<PredicateDefinition> predicates = JSONObject.parseArray(predicate, PredicateDefinition.class);
					definition.setPredicates(predicates);
				} catch (Exception e) {
					log.error("Parse PredicateDefinition meet error! [{}]", e.getMessage(), e);
				}
			}

			// Filter
			String filter = customRoutes.getFilters();
			if (null != filter && !filter.isEmpty()) {
				try {
					List<FilterDefinition> filters = JSONObject.parseArray(filter, FilterDefinition.class);
					definition.setFilters(filters);
				} catch (Exception e) {
					log.error("Parse FilterDefinition meet error! [{}]", e.getMessage(), e);
				}
			}
			
			return definition;
		}
		
		return null;
	}
	
	public static List<RouteDefinition> convertToRouteDefinitionList(List<CustomGatewayRoutes> customGatewayRoutes) {
    	List<RouteDefinition> routeDefinitions = new ArrayList<RouteDefinition>();
    	
    	customGatewayRoutes.stream().forEach(customRoutes -> {
    		RouteDefinition routeDefinition = convertToRouteDefinition(customRoutes);
    		if (null != routeDefinition) {
    			routeDefinitions.add(routeDefinition);
    		}
    	});
    	
		return routeDefinitions;
    }
	
	
}
