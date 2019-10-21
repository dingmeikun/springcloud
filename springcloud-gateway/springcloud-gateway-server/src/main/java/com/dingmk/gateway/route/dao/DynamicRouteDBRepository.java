package com.dingmk.gateway.route.dao;

import java.net.URI;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

import com.alibaba.fastjson.JSONObject;
import com.dingmk.configuration.database.JdbcUtil;
import com.dingmk.gateway.route.dto.CustomGatewayRoutes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class DynamicRouteDBRepository {
	
	private static final String GATEWAY_ROUTES_TABLE = "tb_api_gateway_routes";
	
	private static final String GATEWAY_ROUTES_INFO_MAPPING = "gw.route_id as routeId, gw.route_uri as uri, gw.predicates, gw.filters, gw.orders, gw.description, gw.status, gw.description, gw.createtime as createTime";
	
	private static final String GATEWAY_ROUTES_INSERT_BASE = "insert into " + GATEWAY_ROUTES_TABLE + "(route_id, route_uri, predicates, filters, orders, description, status, createtime)"
			+ " values (?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String GATEWAY_ROUTES_UPDATE_BASE = "update " + GATEWAY_ROUTES_TABLE + " set route_uri = ?, predicates = ?, filters = ?, orders = ?,"
			+ " description = ? where route_id = ?";
	
	private static final String GATEWAY_ROUTES_DELETE_BASE = "delete from " + GATEWAY_ROUTES_TABLE;
	private static final String GATEWAY_ROUTES_QUERY_BASE = "select " + GATEWAY_ROUTES_INFO_MAPPING + " from ";
	
	private static final String GATEWAY_ROUTES_QUERY_LAST = " gw where gw.status=1";
	
	private static final String GATEWAY_ROUTES_QUERY_LAST_WITHID = " gw where gw.route_id = ? and gw.status=1";
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public String deleteByPrimaryKey(String routeId) {
		String sql = GATEWAY_ROUTES_DELETE_BASE + GATEWAY_ROUTES_QUERY_LAST_WITHID;
		return JdbcUtil.queryForObject(jdbcTemplate, sql, new Object[] { routeId }, String.class);
	}

    public int insert(CustomGatewayRoutes routes) {
    	 validateRoutes(routes);
    	
    	Object[] parametes = new Object[] { routes.getRouteId(), routes.getUri(), routes.getPredicates(), routes.getFilters(), routes.getOrders(),
    			routes.getDescription(), routes.getStatus(), routes.getCreateTime()};
    	
    	return jdbcTemplate.update(GATEWAY_ROUTES_INSERT_BASE, parametes);
    }

    public CustomGatewayRoutes selectByPrimaryKey(String routeId) {
    	String sql = GATEWAY_ROUTES_QUERY_BASE + GATEWAY_ROUTES_TABLE + GATEWAY_ROUTES_QUERY_LAST_WITHID;
    	return JdbcUtil.queryForObject(jdbcTemplate, sql, new Object[] { routeId }, CustomGatewayRoutes.class);
    }

    public int updateByPrimaryKey(CustomGatewayRoutes routes) {
    	validateRoutes(routes);
    	
    	Object[] parameters = new Object[] { routes.getUri(), routes.getPredicates(), routes.getFilters(), routes.getOrders(),
    			routes.getDescription(), routes.getRouteId() };
    	
    	return jdbcTemplate.update(GATEWAY_ROUTES_UPDATE_BASE, parameters, new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.INTEGER, Types.VARCHAR, Types.VARCHAR });
    }

    public List<RouteDefinition> findAllRouteDefinition(){
    	String sql = GATEWAY_ROUTES_QUERY_BASE + GATEWAY_ROUTES_TABLE + GATEWAY_ROUTES_QUERY_LAST;
    	List<CustomGatewayRoutes> customGatewayRoutes = JdbcUtil.queryForList(jdbcTemplate, sql, null, CustomGatewayRoutes.class);
    	
    	return convertToRouteDefinition(customGatewayRoutes);
    }
    
    public List<CustomGatewayRoutes> findAllRoutes(){
    	String sql = GATEWAY_ROUTES_QUERY_BASE + GATEWAY_ROUTES_TABLE + GATEWAY_ROUTES_QUERY_LAST;
    	return JdbcUtil.queryForList(jdbcTemplate, sql, null, CustomGatewayRoutes.class);
    }
    
    private void validateRoutes(CustomGatewayRoutes routes) {
    	Assert.notNull(routes, "CustomGatewayRoutes cannot be null");
    	Assert.notNull(routes.getRouteId(), "CustomGatewayRoutes Route-Id cannot be null.");
    	Assert.notNull(routes.getUri(), "CustomGatewayRoutes Route-Uri cannot be null.");
    }
    
    @SuppressWarnings("null")
	private static List<RouteDefinition> convertToRouteDefinition(List<CustomGatewayRoutes> customGatewayRoutes) {
    	List<RouteDefinition> routeDefinitions = new ArrayList<RouteDefinition>();
    	
    	customGatewayRoutes.stream().forEach(customRoutes -> {
    		if (null != customRoutes && null != customRoutes.getRouteId()) {
    			RouteDefinition definition = new RouteDefinition();
    			
    			// ID
    			definition.setId(customRoutes.getRouteId());
    			
    			// URI
    			String uriStr = customRoutes.getUri();
    			if (null != uriStr || !(uriStr = uriStr.trim()).isEmpty()) {
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
    			
    			routeDefinitions.add(definition);
    		}
    	});
    	
		return routeDefinitions;
    }
}
