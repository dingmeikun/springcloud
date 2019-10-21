package com.dingmk.gateway.route.support;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingmk.gateway.route.consts.DynamicRouteConsts;
import com.dingmk.gateway.route.dao.DynamicRouteDBRepository;
import com.dingmk.gateway.route.dto.CustomGatewayRoutes;
import com.dingmk.gateway.route.dto.CustomPageResult;
import com.dingmk.gateway.route.dto.GatewayRoutesVO;
import com.dingmk.gateway.route.model.CustomFilterDefinition;
import com.dingmk.gateway.route.model.CustomPredicateDefinition;
import com.dingmk.gateway.route.model.CustomRouteDefinition;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xy.onlineteam.integration.cache.core.XyCache;
import com.xy.onlineteam.integration.cache.core.XyCacheManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DynamicRouteService implements ApplicationEventPublisherAware, IDynamicRouteService {

	private static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private XyCache xyCache;

	@Resource
	private XyCacheManager cacheManager;

	@PostConstruct
	public void postConstruct() {
		xyCache = cacheManager.getXyCache(DynamicRouteConsts.GATEWAY_ROUTES_PREFIX);
	}
	
	@Resource
	private RouteDefinitionWriter routeDefinitionWriter;

	@Resource
	private DynamicRouteDBRepository routeRepository;

	private ApplicationEventPublisher publisher;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}

	/**
	 * 给spring注册事件 刷新路由
	 */
	private void notifyChanged() {
		this.publisher.publishEvent(new RefreshRoutesEvent(this));
	}

	/**
     * 新增路由
     * 新增规则：1增加DB路由  2增加缓存路由
     *
     * @param gatewayRouteDefinition
     * @return
     */
    @Override
    public String add(CustomRouteDefinition gatewayRouteDefinition) {
        CustomGatewayRoutes CustomGatewayRoutes = transformToCustomGatewayRoutes(gatewayRouteDefinition);
        CustomGatewayRoutes.setStatus(1);
        Date now = new Date();
        CustomGatewayRoutes.setCreateTime(timeFormat.format(now));
        CustomGatewayRoutes.setUpdateTime(timeFormat.format(now));
        routeRepository.insert(CustomGatewayRoutes);

        gatewayRouteDefinition.setId(CustomGatewayRoutes.getRouteId());
       
        xyCache.hset(DynamicRouteConsts.GATEWAY_ROUTES_SUFFIX, gatewayRouteDefinition.getId(), JSONObject.toJSONString(gatewayRouteDefinition));
        
        return CustomGatewayRoutes.getRouteId();
    }

    /**
     * 	修改路由
     * 	修改规则：1 淘汰缓存路由  2修改DB路由
     *
     * @param gatewayRouteDefinition
     * @return
     */
    @Override
    public String update(CustomRouteDefinition gatewayRouteDefinition) {
        CustomGatewayRoutes CustomGatewayRoutes = transformToCustomGatewayRoutes(gatewayRouteDefinition);
        CustomGatewayRoutes.setUpdateTime(timeFormat.format(new Date()));
        xyCache.hdel(DynamicRouteConsts.GATEWAY_ROUTES_SUFFIX, gatewayRouteDefinition.getId());
        
        routeRepository.updateByPrimaryKey(CustomGatewayRoutes);
        
        return gatewayRouteDefinition.getId();
    }


    /**
     *	删除路由
     *	删除规则：1删除DB路由   2删除缓存路由
     *
     * @param 路由routeId
     * @return
     */
    @Override
    public String delete(String routeId) {
    	routeRepository.deleteByPrimaryKey(routeId);
    	xyCache.hdel(DynamicRouteConsts.GATEWAY_ROUTES_SUFFIX, routeId);
        
        return "success";
    }

    /**
     * 	查询全部数据
     *
     * @return
     */
    @Override
    public CustomPageResult<GatewayRoutesVO> findAll(Map<String, Object> params) {
        PageHelper.startPage(MapUtils.getInteger(params, "page"),MapUtils.getInteger(params, "limit"),true);
        List<CustomGatewayRoutes> alls = routeRepository.findAllRoutes();
        
        List<GatewayRoutesVO> routesVO = new ArrayList<GatewayRoutesVO>(alls.size());
        alls.stream().forEach(route -> { // CustomGatewayRoutes -> GatewayRoutesVO
        	GatewayRoutesVO vo = new GatewayRoutesVO();
        	vo.setId(route.getRouteId());
        	// 属性拷贝
        	// TODO 
        	routesVO.add(vo);
        });
        PageInfo<GatewayRoutesVO> pageInfo = new PageInfo<>(routesVO);
        return CustomPageResult.<GatewayRoutesVO>builder().data(pageInfo.getList()).code(0).count(pageInfo.getTotal()).build();
    }

    /**
     * 手工同步路由 
     * 同步规则：DB -> Redis
     * 
     * @return
     */
    @Override
    public String synchronization() {
        List<CustomGatewayRoutes> alls = routeRepository.findAllRoutes();

        for (CustomGatewayRoutes route : alls) {
        	CustomRouteDefinition gatewayRouteDefinition = CustomRouteDefinition.builder()
                    .description(route.getDescription())
                    .id(route.getRouteId())
                    .order(route.getOrders())
                    .uri(route.getUri())
                    .build();

            List<CustomFilterDefinition> gatewayFilterDefinitions = JSONArray.parseArray(route.getFilters(), CustomFilterDefinition.class);
            List<CustomPredicateDefinition> gatewayPredicateDefinitions = JSONArray.parseArray(route.getPredicates(), CustomPredicateDefinition.class);
            gatewayRouteDefinition.setPredicates(gatewayPredicateDefinitions);
            gatewayRouteDefinition.setFilters(gatewayFilterDefinitions);

            xyCache.hset(DynamicRouteConsts.GATEWAY_ROUTES_SUFFIX, route.getRouteId(), JSONObject.toJSONString(gatewayRouteDefinition));
        }

        return "success";
    }

    /**
     * 上线/下线路由
     * 状态:1 启用状态(上线)  0 失效状态(下线)
     * @param params
     * @return
     */
    @Override
    public String updateFlag(Map<String, Object> params) {
        String id = MapUtils.getString(params, "id");
        Integer flag = MapUtils.getInteger(params, "flag");

        CustomGatewayRoutes CustomGatewayRoutes = routeRepository.selectByPrimaryKey(id);
        if (CustomGatewayRoutes == null) {
            return "路由不存在";
        }

        if (flag == 1){
        	xyCache.hdel(DynamicRouteConsts.GATEWAY_ROUTES_SUFFIX, id);
            
        }else {
        	CustomRouteDefinition gatewayRouteDefinition = CustomRouteDefinition.builder()
                    .description(CustomGatewayRoutes.getDescription())
                    .id(CustomGatewayRoutes.getRouteId())
                    .order(CustomGatewayRoutes.getOrders())
                    .uri(CustomGatewayRoutes.getUri())
                    .build();

            List<CustomFilterDefinition> gatewayFilterDefinitions = JSONArray.parseArray(CustomGatewayRoutes.getFilters(), CustomFilterDefinition.class);
            List<CustomPredicateDefinition> gatewayPredicateDefinitions = JSONArray.parseArray(CustomGatewayRoutes.getPredicates(), CustomPredicateDefinition.class);
            gatewayRouteDefinition.setPredicates(gatewayPredicateDefinitions);
            gatewayRouteDefinition.setFilters(gatewayFilterDefinitions);

            xyCache.hset(DynamicRouteConsts.GATEWAY_ROUTES_SUFFIX, CustomGatewayRoutes.getRouteId(), JSONObject.toJSONString(gatewayRouteDefinition));
        }

        CustomGatewayRoutes.setStatus(flag);
        CustomGatewayRoutes.setUpdateTime(timeFormat.format(new Date()));
        int i = routeRepository.updateByPrimaryKey(CustomGatewayRoutes);
        return i > 0 ? "更新成功": "更新失败";
    }

    /**
     *	 转化路由对象  
     *	CustomGatewayRoutes -> CustomGatewayRoutes
     *
     * @param gatewayRouteDefinition
     * @return
     */
    private CustomGatewayRoutes transformToCustomGatewayRoutes(CustomRouteDefinition customRouteDefinition){
        CustomGatewayRoutes customGatewayRoutes = new CustomGatewayRoutes();
        
        customGatewayRoutes.setRouteId(customRouteDefinition.getId());
        if (!StringUtils.isNotBlank(customRouteDefinition.getId())){
        	customGatewayRoutes.setRouteId(java.util.UUID.randomUUID().toString().toUpperCase().replace("-",""));
        }

        String filters = JSONArray.toJSONString(customRouteDefinition.getFilters());
        String predicates = JSONArray.toJSONString(customRouteDefinition.getPredicates());

        customGatewayRoutes.setFilters(filters);
        customGatewayRoutes.setPredicates(predicates);
        customGatewayRoutes.setUri(customRouteDefinition.getUri());
        customGatewayRoutes.setOrders(customRouteDefinition.getOrder());
        customGatewayRoutes.setDescription(customRouteDefinition.getDescription());
        
        return customGatewayRoutes;
    }

	/**
	 * 增加路由
	 * 
	 * @param definition
	 * @return
	 */
	/*
	 * public String add(RouteDefinition definition) {
	 * routeDefinitionWriter.save(Mono.just(definition)).subscribe();
	 * this.publisher.publishEvent(new RefreshRoutesEvent(this)); return "success";
	 * }
	 * 
	 *//**
		 * 更新路由
		 * 
		 * @param definition
		 * @return
		 */
	/*
	 * public String update(RouteDefinition definition) { try {
	 * this.routeDefinitionWriter.delete(Mono.just(definition.getId())); } catch
	 * (Exception e) { return
	 * "update fail,not find route  routeId: "+definition.getId(); } try {
	 * routeDefinitionWriter.save(Mono.just(definition)).subscribe();
	 * this.publisher.publishEvent(new RefreshRoutesEvent(this)); return "success";
	 * } catch (Exception e) { return "update route fail"; } }
	 * 
	 *//**
		 * 删除路由
		 * 
		 * @param id
		 * @return
		 *//*
			 * public String delete(String id) {
			 * this.routeDefinitionWriter.delete(Mono.just(id));
			 * this.publisher.publishEvent(new RefreshRoutesEvent(this)); return
			 * "delete success"; }
			 */
}
