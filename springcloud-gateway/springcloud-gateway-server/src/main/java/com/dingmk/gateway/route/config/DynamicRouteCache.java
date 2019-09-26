package com.dingmk.gateway.route.config;

import static java.util.Collections.synchronizedMap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.util.CollectionUtils;

public class DynamicRouteCache {

	public static Map<String, RouteDefinition> storeRoutes = synchronizedMap(new LinkedHashMap<String, RouteDefinition>());
	
	public static List<RouteDefinition> getRouteDefinitions() {
		List<RouteDefinition> routeDefinitionList = new ArrayList<RouteDefinition>();
		if (CollectionUtils.isEmpty(storeRoutes)) {
			return routeDefinitionList;
		}
		
		storeRoutes.keySet().stream().forEach(route -> {
			if (null != route && !route.trim().isEmpty()) {
				routeDefinitionList.add(storeRoutes.get(route));
			}
		});
		return routeDefinitionList;
	}
}
