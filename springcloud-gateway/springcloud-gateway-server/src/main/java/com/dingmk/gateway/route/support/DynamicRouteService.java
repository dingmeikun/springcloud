package com.dingmk.gateway.route.support;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingmk.gateway.route.dao.DynamicRouteRepository;
import com.dingmk.gateway.route.dto.CustomGatewayRoutes;
import com.dingmk.gateway.route.dto.CustomPageResult;
import com.dingmk.gateway.route.dto.GatewayRoutesVO;
import com.dingmk.gateway.route.model.CustomFilterDefinition;
import com.dingmk.gateway.route.model.CustomPredicateDefinition;
import com.dingmk.gateway.route.model.CustomRouteDefinition;
import com.fasterxml.jackson.databind.BeanProperty;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class DynamicRouteService implements ApplicationEventPublisherAware, IDynamicRouteService {

	public static final String GATEWAY_ROUTES_PREFIX = "GETEWAY_ROUTES";
	
	@Autowired
	private StringRedisTemplate redisTemplate;

	@Resource
	private RouteDefinitionWriter routeDefinitionWriter;

	@Resource
	private DynamicRouteRepository routeRepository;

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
     *
     * @param gatewayRouteDefinition
     * @return
     */
    @Override
    public String add(CustomRouteDefinition gatewayRouteDefinition) {
        CustomGatewayRoutes CustomGatewayRoutes = transformToCustomGatewayRoutes(gatewayRouteDefinition);
        CustomGatewayRoutes.setDelFlag(0);
        CustomGatewayRoutes.setCreateTime(new Date());
        CustomGatewayRoutes.setUpdateTime(new Date());
        routeRepository.insertSelective(CustomGatewayRoutes);

        gatewayRouteDefinition.setId(CustomGatewayRoutes.getId());
       
        redisTemplate.boundHashOps(GATEWAY_ROUTES_PREFIX).put(gatewayRouteDefinition.getId(),  JSONObject.toJSONString(gatewayRouteDefinition));
        
        return CustomGatewayRoutes.getId();
    }

    /**
     * 修改路由
     *
     * @param gatewayRouteDefinition
     * @return
     */
    @Override
    public String update(CustomRouteDefinition gatewayRouteDefinition) {
        CustomGatewayRoutes CustomGatewayRoutes = transformToCustomGatewayRoutes(gatewayRouteDefinition);
        CustomGatewayRoutes.setCreateTime(new Date());
        CustomGatewayRoutes.setUpdateTime(new Date());
        routeRepository.updateByPrimaryKeySelective(CustomGatewayRoutes);

        
    	redisTemplate.boundHashOps(GATEWAY_ROUTES_PREFIX).delete(gatewayRouteDefinition.getId()); 
        
        redisTemplate.boundHashOps(GATEWAY_ROUTES_PREFIX).put(gatewayRouteDefinition.getId(),  JSONObject.toJSONString(gatewayRouteDefinition));
        
        
        return gatewayRouteDefinition.getId();
    }


    /**
     * 删除路由
     * @param id
     * @return
     */
    @Override
    public String delete(String id) {
    	routeRepository.deleteByPrimaryKey(id);
        redisTemplate.boundHashOps(GATEWAY_ROUTES_PREFIX).delete( id ); 
        
        return "success";
//        try {
//            this.routeDefinitionWriter.delete(Mono.just(id));
//            notifyChanged();
//            return "delete success";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "delete fail";
//        }
    }

    /**
     * 查询全部数据
     *
     * @return
     */
    @Override
    public CustomPageResult<GatewayRoutesVO> findAll(Map<String, Object> params) {
        PageHelper.startPage(MapUtils.getInteger(params, "page"),MapUtils.getInteger(params, "limit"),true);
        List<CustomGatewayRoutes> alls = routeRepository.findAll(new HashMap());
        
        List<GatewayRoutesVO> routesVO = new ArrayList<GatewayRoutesVO>(alls.size());
        alls.stream().forEach(route -> { // CustomGatewayRoutes -> GatewayRoutesVO
        	GatewayRoutesVO vo = new GatewayRoutesVO();
        	vo.setId(route.getId());
        	// 属性拷贝
        	routesVO.add(vo);
        });
        PageInfo<GatewayRoutesVO> pageInfo = new PageInfo<>(routesVO);
        return CustomPageResult.<GatewayRoutesVO>builder().data(pageInfo.getList()).code(0).count(pageInfo.getTotal()).build();
    }

    /**
     * @return
     */
    @Override
    public String synchronization() {
        HashMap map = new HashMap();
        map.put("delFlag", 0);
        List<CustomGatewayRoutes> alls = routeRepository.findAll(map);

        for (CustomGatewayRoutes route : alls) {
        	CustomRouteDefinition gatewayRouteDefinition = CustomRouteDefinition.builder()
                    .description(route.getDescription())
                    .id(route.getId())
                    .order(route.getOrder())
                    .uri(route.getUri())
                    .build();

            List<CustomFilterDefinition> gatewayFilterDefinitions = JSONArray.parseArray(route.getFilters(), CustomFilterDefinition.class);
            List<CustomPredicateDefinition> gatewayPredicateDefinitions = JSONArray.parseArray(route.getPredicates(), CustomPredicateDefinition.class);
            gatewayRouteDefinition.setPredicates(gatewayPredicateDefinitions);
            gatewayRouteDefinition.setFilters(gatewayFilterDefinitions);

            redisTemplate.boundHashOps(GATEWAY_ROUTES_PREFIX).put( route.getId() ,  JSONObject.toJSONString(gatewayRouteDefinition));
        }

        return "success";
    }

    /**
     * 更改路由状态
     *
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
            redisTemplate.boundHashOps(GATEWAY_ROUTES_PREFIX).delete( id ); 
            
        }else {
        	CustomRouteDefinition gatewayRouteDefinition = CustomRouteDefinition.builder()
                    .description(CustomGatewayRoutes.getDescription())
                    .id(CustomGatewayRoutes.getId())
                    .order(CustomGatewayRoutes.getOrder())
                    .uri(CustomGatewayRoutes.getUri())
                    .build();

            List<CustomFilterDefinition> gatewayFilterDefinitions = JSONArray.parseArray(CustomGatewayRoutes.getFilters(), CustomFilterDefinition.class);
            List<CustomPredicateDefinition> gatewayPredicateDefinitions = JSONArray.parseArray(CustomGatewayRoutes.getPredicates(), CustomPredicateDefinition.class);
            gatewayRouteDefinition.setPredicates(gatewayPredicateDefinitions);
            gatewayRouteDefinition.setFilters(gatewayFilterDefinitions);

            redisTemplate.boundHashOps(GATEWAY_ROUTES_PREFIX).put( CustomGatewayRoutes.getId() ,  JSONObject.toJSONString(gatewayRouteDefinition));
            
        }

        CustomGatewayRoutes.setDelFlag(flag);
        CustomGatewayRoutes.setUpdateTime(new Date());
        int i = routeRepository.updateByPrimaryKeySelective(CustomGatewayRoutes);
        return i > 0 ? "更新成功": "更新失败";
    }

    /**
     * 转化路由对象  CustomGatewayRoutes
     * @param gatewayRouteDefinition
     * @return
     */
    private CustomGatewayRoutes transformToCustomGatewayRoutes(CustomRouteDefinition customRouteDefinition){
        CustomGatewayRoutes customGatewayRoutes = new CustomGatewayRoutes();
        
        //设置路由id
        if (!StringUtils.isNotBlank(customGatewayRoutes.getId())){
        	customGatewayRoutes.setId(java.util.UUID.randomUUID().toString().toUpperCase().replace("-",""));
        }

        String filters = JSONArray.toJSONString(customRouteDefinition.getFilters());
        String predicates = JSONArray.toJSONString(customRouteDefinition.getPredicates());

        customGatewayRoutes.setFilters(filters);
        customGatewayRoutes.setPredicates(predicates);
        customGatewayRoutes.setUri(customRouteDefinition.getUri());
        customGatewayRoutes.setOrder(customRouteDefinition.getOrder());
        customGatewayRoutes.setDescription(customRouteDefinition.getDescription());

        return customGatewayRoutes;
    }

    /**
     * 测试方法 新建 一个路由
     */
//    @PostConstruct
    public void main() {
        RouteDefinition definition = new RouteDefinition();
        definition.setId("jd");
        URI uri = UriComponentsBuilder.fromUriString("lb://user-center").build().toUri();
//         URI uri = UriComponentsBuilder.fromHttpUrl("http://baidu.com").build().toUri();
        definition.setUri(uri);
        definition.setOrder(11111);

        //定义第一个断言
        PredicateDefinition predicate = new PredicateDefinition();
        predicate.setName("Path");

        Map<String, String> predicateParams = new HashMap<>(8);
        predicateParams.put("pattern", "/jd/**");
        predicate.setArgs(predicateParams);
        //定义Filter
        FilterDefinition filter = new FilterDefinition();
        filter.setName("StripPrefix");
        Map<String, String> filterParams = new HashMap<>(8);
        //该_genkey_前缀是固定的，见org.springframework.cloud.gateway.support.NameUtils类
        filterParams.put("_genkey_0", "1");
        filter.setArgs(filterParams);

        definition.setFilters(Arrays.asList(filter));
        definition.setPredicates(Arrays.asList(predicate));

        System.out.println("definition:" + JSON.toJSONString(definition));
        redisTemplate.opsForHash().put(GATEWAY_ROUTES_PREFIX, "key", JSON.toJSONString(definition));
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
