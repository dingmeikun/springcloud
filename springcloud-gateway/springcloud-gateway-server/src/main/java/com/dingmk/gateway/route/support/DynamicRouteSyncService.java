package com.dingmk.gateway.route.support;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dingmk.comm.utils.JsonMapper;
import com.dingmk.gateway.route.consts.DynamicRouteConsts;
import com.dingmk.gateway.route.dao.DynamicRouteDBRepository;
import com.dingmk.gateway.route.dto.CustomGatewayRoutes;
import com.dingmk.gateway.route.model.CustomRouteDefinition;
import com.dingmk.gateway.route.utils.CommonUtil;
import com.dingmk.gateway.route.utils.IOUtil;
import com.dingmk.redis.XyJedis;
import com.dingmk.redis.XyJedisPool;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Pipeline;

@Slf4j
@Service
public class DynamicRouteSyncService {

	@Resource
    private XyJedisPool jedisPool;
	
	@Resource
	private DynamicRouteDBRepository routeRepository;
	
	public void syncRedis() {
		List<CustomGatewayRoutes> dbRoutes = routeRepository.findAllRoutes();
		if (CollectionUtils.isEmpty(dbRoutes)) {
			log.debug("RouteSync Timer Sync Stopping! There is no route data in DB!!");
			return;
		}
		
		XyJedis jedis = null;
        Pipeline pipelined = null;
    	try {
            jedis = jedisPool.getResource();
            pipelined = jedis.pipelined();
            
            Long count = jedis.hlen(DynamicRouteConsts.GATEWAY_ROUTES_HASH_KEY);
            if (count > 0) {
            	log.debug("[SYNC] Clear Redis cache, remove count:{}", count);
            	jedis.del(DynamicRouteConsts.GATEWAY_ROUTES_HASH_KEY);
            }
            
            log.debug("RouteSync Timer Sync Data Begin! sync count:{}", dbRoutes.size());
    		for (CustomGatewayRoutes dbRoute : dbRoutes) {
    			CustomRouteDefinition redisGatewayRoutes = CommonUtil.convertToCustomRouteDefinition(dbRoute);
                
    			if (null != redisGatewayRoutes && redisGatewayRoutes.valide()) {
    				jedis.hset(DynamicRouteConsts.GATEWAY_ROUTES_HASH_KEY, redisGatewayRoutes.getId(), JsonMapper.nonDefaultMapper().toJson(redisGatewayRoutes));
    			}
    		}
    		pipelined.syncAndReturnAll();
    	} catch (Exception e) {
    		log.error("Sync DB routes into Redis meet exception! [{}]", e.getMessage(), e);
    	} finally {
    		IOUtil.closeQuietly(pipelined, jedis);
    		log.debug("RouteSync Timer Sync Data end!");
    	}
	}
}
