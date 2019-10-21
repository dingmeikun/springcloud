package com.dingmk.gateway.route.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import com.dingmk.comm.utils.JsonMapper;
import com.dingmk.gateway.route.common.BasicModelConfig;
import com.dingmk.gateway.route.consts.DynamicRouteConsts;
import com.dingmk.gateway.route.dao.DynamicRouteDBRepository;
import com.dingmk.gateway.route.dto.CustomGatewayRoutes;
import com.dingmk.gateway.route.model.CustomRouteDefinition;
import com.dingmk.gateway.route.utils.CommonUtil;
import com.dingmk.redis.XyJedis;
import com.dingmk.redis.XyJedisPool;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class DynamicDefinitionRepository implements RouteDefinitionRepository {

	private static volatile boolean isCacheHit;
	
	@Resource
	private DynamicRouteDBRepository routeRepository;

	@Resource
	private BasicModelConfig baseConfig;
	
	@Resource
    private XyJedisPool jedisPool;

	/** 缓存过期时间 */
	@Value("${gateway.local.cache.expire}")
	private int expire;

	/** 缓存初始大小 */
	@Value("${gateway.local.cache.capacity}")
	private int capacity;
	
	/**
	 * <b> BUG 指定size和expire不生效  </b>
	 */
	private Cache<String, Map<String, RouteDefinition>> cache = CacheBuilder.newBuilder()
            .initialCapacity(10).expireAfterWrite(60, TimeUnit.SECONDS).build();
	
	/**
	 * 获取全部路由
	 * <b>使用v2缓存框架：先获取本地缓存，拿不到再获取redis缓存，还获取不到就回调获取DB路由信息(框架层设置回缓存)</b>
	 * 
	 * @return
	 */
	@Override
	public Flux<RouteDefinition> getRouteDefinitions() {
		try {
			Map<String, RouteDefinition> cacheRoutes = cache.get(DynamicRouteConsts.L_GATEWAY_ROUTES_HASH_KEY,
					new Callable<Map<String, RouteDefinition>>() {

						@Override
						public Map<String, RouteDefinition> call() throws Exception {
							
							Map<String, RouteDefinition> storeRoutes = new HashMap<String, RouteDefinition>();
							try (XyJedis jedis = jedisPool.getResource()) {
								Map<String, String> routes = jedis.hgetAll(DynamicRouteConsts.GATEWAY_ROUTES_HASH_KEY);
								
								routes.forEach((routeId, route) -> {
									if (null != routeId && !routeId.isEmpty() && null != route) {
										CustomRouteDefinition customRoute = JsonMapper.nonDefaultMapper().fromJson(route, CustomRouteDefinition.class);
										if (null != customRoute && customRoute.valide()) {
											RouteDefinition routeDefinition = CommonUtil.convertToRouteDefinition(customRoute);
											if (null != routeDefinition) {
												storeRoutes.put(routeId, routeDefinition);
											}
										}
									}
								});
					        } catch (Exception e) {
					        	log.error("XYJEDIS_MEET_EXCEPTION:", e.getMessage(), e);
							}
							isCacheHit = false;
							return storeRoutes;
						}
					});

			if (CollectionUtils.isEmpty(cacheRoutes)) {
				Map<String, RouteDefinition> dbMapRoutes = queryDBRoutesInfo();
				if (CollectionUtils.isEmpty(dbMapRoutes)) {
					return Flux.empty();
				}

				putCache(dbMapRoutes);
				return Flux.fromIterable(dbMapRoutes.values());
			}

			if (!isCacheHit) {
				putCache(cacheRoutes);
			}
			
			return Flux.fromIterable(cacheRoutes.values());
		} catch (Exception e) {
			log.error("ROUTES_DEFINITIONS_EXCEPTION:{}", e.getMessage(), e);
			
			Map<String, RouteDefinition> dbMapRoutes = queryDBRoutesInfo();
			if (CollectionUtils.isEmpty(dbMapRoutes)) {
				return Flux.empty();
			}

			putCache(dbMapRoutes);
			return Flux.fromIterable(dbMapRoutes.values());
		} finally {
			isCacheHit = true;
		}
	}
	
	private void putCache(Map<String, RouteDefinition> cacheValue) {
		cache.put(DynamicRouteConsts.L_GATEWAY_ROUTES_HASH_KEY, cacheValue);
	}
	
	private Map<String, RouteDefinition> queryDBRoutesInfo() {
		Map<String, RouteDefinition> dbMapRoutes = new HashMap<String, RouteDefinition>();
		
		if (baseConfig.getGlobalLimiter().tryAcquire()) {
			List<CustomGatewayRoutes> dbRoutes = routeRepository.findAllRoutes();
			if (CollectionUtils.isEmpty(dbRoutes)) {
				return Collections.emptyMap();
			}
			
			List<RouteDefinition> definition = CommonUtil.convertToRouteDefinitionList(dbRoutes);
			definition.stream().forEach(route -> {
				if (null != route && null != route.getId() && !route.getId().isEmpty()) {
					dbMapRoutes.put(route.getId(), route);
				}
			});
		}
		
		return dbMapRoutes;
	}

	/**
	 * 添加路由方法
	 * <b>已实现路由存储和读取，默认方法返回默认empty</b>
	 * 
	 * @param route
	 * @return
	 */
	@Override
	public Mono<Void> save(Mono<RouteDefinition> route) {
		return Mono.empty();
	}

	/**
	 * 删除路由
	 * 
	 * @param routeId
	 * @return
	 */
	@Override
	public Mono<Void> delete(Mono<String> routeId) {
		return Mono.empty();
	}
}
