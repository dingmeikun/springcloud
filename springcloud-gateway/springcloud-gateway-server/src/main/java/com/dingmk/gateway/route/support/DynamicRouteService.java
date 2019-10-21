package com.dingmk.gateway.route.support;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dingmk.cache.core.XyCacheManager;
import com.dingmk.comm.constvar.ResultConstVar;
import com.dingmk.comm.utils.JsonMapper;
import com.dingmk.gateway.route.common.BasicLogicResult;
import com.dingmk.gateway.route.consts.DynamicRouteConsts;
import com.dingmk.gateway.route.dao.DynamicRouteDBRepository;
import com.dingmk.gateway.route.dto.CustomGatewayRoutes;
import com.dingmk.gateway.route.model.CustomRouteDefinition;
import com.dingmk.gateway.route.utils.CommonUtil;
import com.dingmk.gateway.route.utils.IOUtil;
import com.dingmk.redis.XyJedis;
import com.dingmk.redis.XyJedisPool;
import com.google.common.base.Objects;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Pipeline;

@Slf4j
@Service
public class DynamicRouteService implements IDynamicRouteService {
	
	private static final BasicLogicResult<String> NULL_ARGUMENT = new BasicLogicResult<>(ResultConstVar.NULL_ARGUMENT,
            "NULL_ARGUMENT");
	
	private static final BasicLogicResult<String> INVALID_ARGUMENT = new BasicLogicResult<>(ResultConstVar.INVALID_ARGUMENT,
            "INVALID_ARGUMENT");
	
	private static final BasicLogicResult<String> INVALID_DB_ROUTE_ARGUMENT = new BasicLogicResult<>(ResultConstVar.INVALID_ARGUMENT,
            "INVALID_DB_ROUTE_ARGUMENT");
	
	private static final BasicLogicResult<String> INVALID_REDIS_ROUTE_ARGUMENT = new BasicLogicResult<>(ResultConstVar.INVALID_ARGUMENT,
            "INVALID_REDIS_ROUTE_ARGUMENT");
	
	@Resource
    private XyJedisPool jedisPool;
	
	@Resource
	private XyCacheManager cacheManager;

	@Resource
	private RouteDefinitionWriter routeDefinitionWriter;

	@Resource
	private DynamicRouteDBRepository routeRepository;

	/**
     * 新增路由
     * 新增规则：1增加DB路由  2增加缓存路由
     *
     * @param gatewayRouteDefinition
     * @return
     */
    @Override
    public BasicLogicResult<String> add(CustomRouteDefinition customRouteDefinition) {
    	CustomGatewayRoutes customGatewayRoutes = CommonUtil.convertToCustomDefinition(customRouteDefinition);
        if (null == customGatewayRoutes) {
        	log.warn("[ADD] Add CustomRouteDefinition is Invalid! please double check!!");
        	return NULL_ARGUMENT;
        }
    	
    	try (XyJedis jedis = jedisPool.getResource()) {
    		SimpleDateFormat timeFormat = new SimpleDateFormat(DynamicRouteConsts.GATEWAY_ROUTES_DATE_FORMAT);
    		customGatewayRoutes.setStatus(1);
            customGatewayRoutes.setCreateTime(timeFormat.format(new Date()));
            
            if (customGatewayRoutes.valide()) {
            	int success = routeRepository.add(customGatewayRoutes);
            	
            	log.debug("[ADD] Gateway ADD Route[{}] into DB Success! flag:{}", customGatewayRoutes.getRouteId(), success);
            	if (1 == success) {
            		if (customRouteDefinition.valide()) {
            			jedis.hset(DynamicRouteConsts.GATEWAY_ROUTES_HASH_KEY, customRouteDefinition.getId(), JsonMapper.nonDefaultMapper().toJson(customRouteDefinition));
            			log.debug("Gateway ADD Route[{}] into Redis Success!", customGatewayRoutes.getRouteId());
            			return new BasicLogicResult<String>(ResultConstVar.OK, "ADD CustomRoute Success!", "RouteID is " + customGatewayRoutes.getRouteId());
            		}
            		return INVALID_REDIS_ROUTE_ARGUMENT;
            	}
            	return new BasicLogicResult<String>(ResultConstVar.ERROR, "ADD CustomRoute Into DB Failed! please check!!");
            }
            return INVALID_DB_ROUTE_ARGUMENT;
    	} catch (Exception e) {
    		log.error("[ADD] Add Route[{}] failed!, Error Message:{}", customRouteDefinition.getId(), e.getMessage(), e);
    		return new BasicLogicResult<String>(ResultConstVar.EXCEPTION, "ADD CustomRoute Meet Exception!", e.getMessage());
    	}
    }

    /**
     * 	修改路由
     * 	修改规则：1 淘汰缓存路由  2修改DB路由
     *
     * @param gatewayRouteDefinition
     * @return
     */
    @Override
    public BasicLogicResult<String> update(CustomRouteDefinition customRouteDefinition) {
    	CustomGatewayRoutes customGatewayRoutes = CommonUtil.convertToCustomDefinition(customRouteDefinition);
    	if (null == customGatewayRoutes) {
    		log.warn("[UPDATE] Update CustomRouteDefinition is Invalid! please double check!!");
    		return NULL_ARGUMENT;
    	}
    	
    	try (XyJedis jedis = jedisPool.getResource()) {
    		jedis.hdel(DynamicRouteConsts.GATEWAY_ROUTES_HASH_KEY, customGatewayRoutes.getRouteId());
    	} catch (Exception e) {
    		log.error("[UPDATE] Remove Route[{}] from Redis failed!, Error Message:{}", customGatewayRoutes.getRouteId(), e.getMessage(), e);
    		return new BasicLogicResult<String>(ResultConstVar.EXCEPTION, "Remove Route" + customGatewayRoutes.getRouteId() + " from Redis Meet Exception!", e.getMessage());
    	}
    	
    	log.debug("[UPDATE] RouteId[{}] is remove from Redis!", customGatewayRoutes.getRouteId());
    	try {
    		routeRepository.updateByRouteId(customGatewayRoutes);
    	} catch (Exception e) {
    		log.error("[UPDATE] Update Route[{}] from DB failed!, Error Message:{}", customGatewayRoutes.getRouteId(), e.getMessage(), e);
    		return new BasicLogicResult<String>(ResultConstVar.EXCEPTION, "Update Route" + customGatewayRoutes.getRouteId() + " from DB Meet Exception!", e.getMessage());
    	}
        
    	return new BasicLogicResult<String>(ResultConstVar.OK, "UPDATE CustomRoute Success!", "RouteID is " + customGatewayRoutes.getRouteId());
    }


    /**
     *	删除路由
     *	删除规则：1删除DB路由   2删除缓存路由 (为后续管理使用)
     *
     * @param 路由routeId
     * @return
     */
    @Override
    public BasicLogicResult<String> delete(String routeId) {
    	if (null == routeId || routeId.trim().isEmpty()) {
    		log.warn("[DELETE] delete RouteId[{}] is Invalid! please double check!!", routeId);
    		return NULL_ARGUMENT;
    	}
    	
    	try (XyJedis jedis = jedisPool.getResource()) {
    		int success = routeRepository.deleteByRouteId(routeId);
    		
    		log.debug("[DELETE] Gateway delete Route[{}] from DB Success! flag:{}", routeId, success);
        	if (1 == success) {
        		jedis.hdel(DynamicRouteConsts.GATEWAY_ROUTES_HASH_KEY, routeId);
        		return new BasicLogicResult<String>(ResultConstVar.OK, "DELETE Route Success!", "RouteID is " + routeId);
        	}
        	return new BasicLogicResult<String>(ResultConstVar.ERROR, "DELETE Route From DB Failed! please check!!");
    	} catch (Exception e) {
    		log.error("[DELETE] Remove Route[{}] from DB/Redis failed!, Error Message:{}", routeId, e.getMessage(), e);
    		return new BasicLogicResult<String>(ResultConstVar.EXCEPTION, "Remove Route" + routeId + " from DB/Redis Meet Exception!", e.getMessage());
    	}
    }

    /**
     * 手工同步路由 
     * 同步规则：DB -> Redis
     * 
     * @return
     */
    @Override
    public BasicLogicResult<String> synchronization() {
    	List<CustomGatewayRoutes> dbRoutes = routeRepository.findAllRoutes();
		if (CollectionUtils.isEmpty(dbRoutes)) {
			log.warn("[SYNC] Query Routes Sets from DB is empty! please double check DB Routes!!");
			return NULL_ARGUMENT;
		}
    	
		int syncCount = 0;
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
            
    		for (CustomGatewayRoutes dbRoute : dbRoutes) {
    			CustomRouteDefinition redisGatewayRoutes = CommonUtil.convertToCustomRouteDefinition(dbRoute);
                
    			if (null != redisGatewayRoutes && redisGatewayRoutes.valide()) {
    				syncCount ++;
    				jedis.hset(DynamicRouteConsts.GATEWAY_ROUTES_HASH_KEY, redisGatewayRoutes.getId(), JsonMapper.nonDefaultMapper().toJson(redisGatewayRoutes));
    			}
    		}
    		pipelined.syncAndReturnAll();
    	} catch (Exception e) {
    		log.error("[SYNC] Sync DB routes into Redis meet exception! [{}]", e.getMessage(), e);
    		return new BasicLogicResult<String>(ResultConstVar.EXCEPTION, "Sync DB routes into Redis meet exception!", e.getMessage());
    	} finally {
    		IOUtil.closeQuietly(pipelined, jedis);
    	}
    	
    	return new BasicLogicResult<String>(ResultConstVar.OK, "Sync CustomRoute Success!", "sync count is " + syncCount);
    }
    
	@Override
	public BasicLogicResult<String> online(String routeId, int status) {
		if (null == routeId || routeId.trim().isEmpty() || !Objects.equal(status, 1)) {
        	log.warn("[Online] RouteId is empty or status is not eq 1! please double check arguments!!");
        	return INVALID_ARGUMENT;
        }

        CustomGatewayRoutes dbGatewayRoutes = routeRepository.queryByRouteId(routeId, status);
        if (dbGatewayRoutes == null) {
        	log.debug("[Online] RouteId[{}] is not existed in DB!!", routeId);
        	return new BasicLogicResult<String>(ResultConstVar.OK_NO_VALUE, "RouteId[" + routeId + "] and status[" + status + "] is not existed in DB!!");
        }
        
        try (XyJedis jedis = jedisPool.getResource()) {
        	dbGatewayRoutes.setStatus(status);
            int success = routeRepository.updateByRouteId(dbGatewayRoutes);
            if (!Objects.equal(1, success)) {
            	return new BasicLogicResult<String>(ResultConstVar.ERROR, "Online Route From DB Failed! please check your routeId and DB's status!!");
            }
        	
            CustomRouteDefinition redisGatewayRoutes = CommonUtil.convertToCustomRouteDefinition(dbGatewayRoutes);
    		if (null != redisGatewayRoutes && redisGatewayRoutes.valide()) {
    			jedis.hset(DynamicRouteConsts.GATEWAY_ROUTES_HASH_KEY, dbGatewayRoutes.getRouteId(), JsonMapper.nonDefaultMapper().toJson(redisGatewayRoutes));
    		} else {
    			return INVALID_REDIS_ROUTE_ARGUMENT;
    		}
    	} catch (Exception e) {
    		log.error("[Online] RouteId[{}] meet exception!, Error Message:{}", routeId, e.getMessage(), e);
    		return new BasicLogicResult<String>(ResultConstVar.EXCEPTION, "UpdateFlag for RouteId[" + routeId + "] meet exception!", e.getMessage());
    	}
        
        return new BasicLogicResult<String>(ResultConstVar.OK, "Online Route Success!", ("RouteId is " + routeId + ", status is " + status));
	}

	@Override
	public BasicLogicResult<String> offline(String routeId, int status) {
		if (null == routeId || routeId.trim().isEmpty() || !Objects.equal(status, 0)) {
        	log.warn("[OFFLINE] RouteId is empty or status is not eq 0! please double check arguments!!");
        	return INVALID_ARGUMENT;
        }

        CustomGatewayRoutes dbGatewayRoutes = routeRepository.queryByRouteId(routeId, status);
        if (dbGatewayRoutes == null) {
        	log.debug("[OFFLINE] RouteId[{}] is not existed in DB!!", routeId);
        	return new BasicLogicResult<String>(ResultConstVar.OK_NO_VALUE, "RouteId[" + routeId + "] and status[" + status + "] is not existed in DB!!");
        }
        
        try (XyJedis jedis = jedisPool.getResource()) {
        	dbGatewayRoutes.setStatus(status);
            int success = routeRepository.updateByRouteId(dbGatewayRoutes);
            if (!Objects.equal(1, success)) {
            	return new BasicLogicResult<String>(ResultConstVar.ERROR, "OFFLINE Route From DB Failed! please check your routeId and DB's status!!");
            }
        	
            int dbStatus = dbGatewayRoutes.getStatus();
        	if (Objects.equal(0, dbStatus)) {
        		if (jedis.hexists(DynamicRouteConsts.GATEWAY_ROUTES_HASH_KEY, routeId)) {
        			jedis.hdel(DynamicRouteConsts.GATEWAY_ROUTES_HASH_KEY, routeId);
        		}
        	}
    	} catch (Exception e) {
    		log.error("[OFFLINE] Offline RouteId[{}] meet exception!, Error Message:{}", routeId, e.getMessage(), e);
    		return new BasicLogicResult<String>(ResultConstVar.EXCEPTION, "Offline RouteId[" + routeId + "] meet exception!", e.getMessage());
    	}
        
        return new BasicLogicResult<String>(ResultConstVar.OK, "Offline Route Success!", ("RouteId is " + routeId + ", status is " + status));
	}
}
