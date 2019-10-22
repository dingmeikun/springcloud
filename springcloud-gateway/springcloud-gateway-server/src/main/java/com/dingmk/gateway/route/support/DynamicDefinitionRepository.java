package com.dingmk.gateway.route.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

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
	
	/**
	 * <b> 初始化本地缓存配置：最大路由size为10，本次缓存时间60秒(默认) </b>
	 */
	private Cache<String, Map<String, RouteDefinition>> cache = CacheBuilder.newBuilder()
            .initialCapacity(10).expireAfterWrite(60, TimeUnit.SECONDS).build();
	
	@PostConstruct
	public void initCache() {
		cache = CacheBuilder.newBuilder()
	            .initialCapacity(baseConfig.getCacheCapacity()).expireAfterWrite(baseConfig.getCacheExpire(), TimeUnit.SECONDS).build();
	}
	
	/**
	 * 获取全部路由
	 * <b>
	 * 	1.获取本地缓存数据，如有则直接返回，如没有执行2
	 * 	2.获取redis数据，返回数据，执行3
	 * 	3.以上1,2如果都没数据，则查询db，返回db数据并将数据插入到本地缓存(如果有数据且为redis缓存，则也插入到本地缓存)
	 *	4.如果发生异常则查询数据库获取数据(查询数据库已设置300qps限制)
	 * </b>
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
		} catch (ExecutionException e1) {
			log.error("ROUTES_DEFINITIONS_LOCALCACHE MEET EXECUTION EXCEPTION! {}", e1.getMessage(), e1);
			return Flux.empty();
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
