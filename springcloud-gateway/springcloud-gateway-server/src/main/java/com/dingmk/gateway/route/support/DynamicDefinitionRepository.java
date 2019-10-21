package com.dingmk.gateway.route.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache.ValueRetrievalException;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.dingmk.gateway.route.consts.DynamicRouteConsts;
import com.dingmk.gateway.route.dao.DynamicRouteDBRepository;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.xy.onlineteam.configuration.redis.XyJedis;
import com.xy.onlineteam.configuration.redis.XyJedisPool;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class DynamicDefinitionRepository implements RouteDefinitionRepository {

	@Resource
	private DynamicRouteDBRepository routeRepository;

	@Resource
    private XyJedisPool jedisPool;

	/** 缓存过期时间 */
	@Value("${spring.data.cache.expire:30}")
	private int expire_time;

	/** 缓存初始大小 */
	@Value("${spring.data.cache.capacity:1}")
	private int max_numsize;

	private Cache<String, Map<String, RouteDefinition>> localcache = CacheBuilder.newBuilder().maximumSize(max_numsize)
			.expireAfterWrite(expire_time, TimeUnit.SECONDS).build();

	/**
	 * 获取全部路由
	 * 
	 * 实现过程： 
	 * 1.使用本地缓存，google的guava的本地cache，可以设置过期时间(1分钟清理一次/1分钟过期)
	 * 2.本方法中，先拿cache，如果为空，则从redis中获取，为空则再从DB中获取，再塞到本地缓存中 
	 * 3.记得在update/add的地方清空本地缓存？update/delete/add时，需要保证redis和db的强一致性！
	 * 
	 * @return
	 */
	@Override
	public Flux<RouteDefinition> getRouteDefinitions() {
		try {
			Map<String, RouteDefinition> cacheRoutes = localcache.get(DynamicRouteConsts.GATEWAY_ROUTES_PREFIX,
					new Callable<Map<String, RouteDefinition>>() {

						@Override
						public Map<String, RouteDefinition> call() throws Exception {
							
							Map<String, RouteDefinition> storeRoutes = new HashMap<String, RouteDefinition>();
							try (XyJedis jedis = jedisPool.getResource()) {
								Map<String, String> routes = jedis.hgetAll(DynamicRouteConsts.GATEWAY_ROUTES_SUFFIX);
								
								routes.forEach((routeId, route) -> {
									if (null != routeId && !routeId.isEmpty() && null != route) {
										RouteDefinition routeDefinition = JSONObject.parseObject(routes.get(routeId),
												RouteDefinition.class);
										if (null != routeDefinition) {
											storeRoutes.put(routeId, routeDefinition);
										}
									}
								});
					        } catch (Exception e) {
					        	log.error("XYJEDIS_MEET_EXCEPTION:", e.getMessage(), e);
							}
							return storeRoutes;
						}
					});

			if (CollectionUtils.isEmpty(cacheRoutes)) {
				List<RouteDefinition> dbRoutes = routeRepository.findAllRouteDefinition();
				if (CollectionUtils.isEmpty(dbRoutes)) {
					return Flux.empty();
				}

				Map<String, RouteDefinition> dbMapRoutes = new HashMap<String, RouteDefinition>();
				dbRoutes.stream().forEach(route -> {
					if (null != route && null != route.getId() && !route.getId().isEmpty()) {
						dbMapRoutes.put(route.getId(), route);
					}
				});

				localcache.put(DynamicRouteConsts.GATEWAY_ROUTES_PREFIX, dbMapRoutes);
				return Flux.fromIterable(dbRoutes);
			}

			return Flux.fromIterable(cacheRoutes.values());
		} catch (ValueRetrievalException e) {
			log.error("ROUTES_DEFINITIONS_MEET_VALUE_RETRIEVAL_EXCEPTION:{}", e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error("ROUTES_DEFINITIONS_EXCEPTION:{}", e.getMessage(), e);
			List<RouteDefinition> routes = routeRepository.findAllRouteDefinition();

			Map<String, RouteDefinition> dbMapRoutes = new HashMap<String, RouteDefinition>();
			routes.stream().forEach(route -> {
				if (null != route && null != route.getId() && !route.getId().isEmpty()) {
					dbMapRoutes.put(route.getId(), route);
				}
			});

			if (CollectionUtils.isEmpty(dbMapRoutes)) {
				return Flux.empty();
			}

			localcache.put(DynamicRouteConsts.GATEWAY_ROUTES_PREFIX, dbMapRoutes);
			return Flux.fromIterable(dbMapRoutes.values());
		}
	}

	/**
	 * 添加路由方法
	 * 
	 * @param route
	 * @return
	 */
	@Override
	public Mono<Void> save(Mono<RouteDefinition> route) {
		return route.flatMap(routeDefinition -> {
			if (null != routeDefinition) {
				localcache.getIfPresent(DynamicRouteConsts.GATEWAY_ROUTES_PREFIX).put(routeDefinition.getId(),
						routeDefinition);
			}
			return Mono.empty();
		});
	}

	/**
	 * 删除路由
	 * 
	 * @param routeId
	 * @return
	 */
	@Override
	public Mono<Void> delete(Mono<String> routeId) {
		return routeId.flatMap(routid -> {
			if (null != routid && !(routid = routid.trim()).isEmpty()) {
				localcache.getIfPresent(DynamicRouteConsts.GATEWAY_ROUTES_PREFIX).remove(routid);
			}
			return Mono.empty();
		});
	}
}
