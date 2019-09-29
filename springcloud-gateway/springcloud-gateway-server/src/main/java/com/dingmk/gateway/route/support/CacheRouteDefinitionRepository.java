package com.dingmk.gateway.route.support;

import static java.util.Collections.synchronizedMap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.cache.Cache.ValueRetrievalException;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.dingmk.gateway.route.consts.DynamicRouteConsts;
import com.dingmk.gateway.route.dao.DynamicRouteDBRepository;
import com.xy.onlineteam.integration.cache.core.XyCache;
import com.xy.onlineteam.integration.cache.core.XyCacheManager;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CacheRouteDefinitionRepository implements RouteDefinitionRepository {

    @Resource
	private DynamicRouteDBRepository routeRepository;

    private XyCache xyCache;

	@Resource
	private XyCacheManager cacheManager;

	@PostConstruct
	public void postConstruct() {
		xyCache = cacheManager.getXyCache(DynamicRouteConsts.GATEWAY_ROUTES_PREFIX);
	}
    
    public static Map<String, RouteDefinition> storeRoutes = synchronizedMap(new LinkedHashMap<String, RouteDefinition>());

    /**
     * 	获取全部路由
     * @return
     */
    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
    	try {
    		storeRoutes.clear();
        	
    		Map<String, String> routes = xyCache.hgetAll(DynamicRouteConsts.GATEWAY_ROUTES_SUFFIX);
    		
    		if (CollectionUtils.isEmpty(routes)) {
    			List<RouteDefinition> dbRoutes = routeRepository.findAllRouteDefinition();
    			if (CollectionUtils.isEmpty(dbRoutes)) {
    				return Flux.empty();
    			}
    			
    			dbRoutes.stream().forEach(route -> {
        			if (null != route && null != route.getId() && !route.getId().isEmpty()) {
        				storeRoutes.put(route.getId(), route);
        			}
        		});
    			return Flux.fromIterable(storeRoutes.values());
    		}
    		
    		routes.forEach((routeId, route) -> {
    			if (null != routeId && !routeId.isEmpty() && null != route) {
    				RouteDefinition routeDefinition = JSONObject.parseObject(routes.get(routeId), RouteDefinition.class);
    				if (null != routeDefinition) {
    					storeRoutes.put(routeId, routeDefinition);
    				}
    			}
    		});
    	} catch (ValueRetrievalException e) {
    		log.error("ROUTES_DEFINITIONS_MEET_VALUE_RETRIEVAL_EXCEPTION:{}", e.getMessage(), e);
			throw e;
    	} catch (Exception e) {
    		log.error("ROUTES_DEFINITIONS_EXCEPTION:{}", e.getMessage(), e);
    		List<RouteDefinition> routes = routeRepository.findAllRouteDefinition();
			
    		routes.stream().forEach(route -> {
    			if (null != route && null != route.getId() && !route.getId().isEmpty()) {
    				storeRoutes.put(route.getId(), route);
    			}
    		});
    	}
        
        return Flux.fromIterable(storeRoutes.values());
    }

    /**
     * 	添加路由方法
     * @param route
     * @return
     */
    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(routeDefinition -> {
        	if (null != routeDefinition) {
        		storeRoutes.put(routeDefinition.getId(), routeDefinition);
        	}
            return Mono.empty();
        });
    }

    /**
     * 	删除路由
     * @param routeId
     * @return
     */
    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(routid -> {
        	if (null != routid && !(routid = routid.trim()).isEmpty()) {
        		storeRoutes.remove(routid);
        	}
            return Mono.empty();
        });
    }
}
