package com.dingmk.gateway.route.dao;

import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.dingmk.configuration.database.JdbcUtil;
import com.dingmk.gateway.route.dto.CustomGatewayRoutes;
import com.google.common.base.Objects;

@Repository
public class DynamicRouteDBRepository {
	
	private static final String GATEWAY_ROUTES_TABLE = "tb_api_gateway_routes";
	
	private static final String GATEWAY_ROUTES_INFO_MAPPING = "gw.route_id as routeId, gw.route_uri as uri, gw.predicates, gw.filters, gw.orders, gw.description, gw.status, gw.description, gw.createtime as createTime";
	
	private static final String GATEWAY_ROUTES_INSERT_BASE = "insert into " + GATEWAY_ROUTES_TABLE + "(route_id, route_uri, predicates, filters, orders, description, status, createtime)"
			+ " values (?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String GATEWAY_ROUTES_UPDATE_BASE = "update " + GATEWAY_ROUTES_TABLE + " set route_uri = ?, predicates = ?, filters = ?, orders = ?,"
			+ " description = ?, status = ? where route_id = ?";
	
	private static final String GATEWAY_ROUTES_DELETE_BASE = "delete gw from " + GATEWAY_ROUTES_TABLE;
	private static final String GATEWAY_ROUTES_QUERY_BASE = "select " + GATEWAY_ROUTES_INFO_MAPPING + " from ";
	
	private static final String GATEWAY_ROUTES_QUERY_LAST = " gw where gw.status=1";
	
	private static final String GATEWAY_ROUTES_QUERY_LAST_ONLINE_ROUTE = " gw where gw.route_id = ? and gw.status=1";
	
	private static final String GATEWAY_ROUTES_QUERY_LAST_OFFLINE_ROUTE = " gw where gw.route_id = ? and gw.status=0";
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public int deleteByRouteId(String routeId) {
		String sql = GATEWAY_ROUTES_DELETE_BASE + GATEWAY_ROUTES_QUERY_LAST_OFFLINE_ROUTE;
		return jdbcTemplate.update(sql, new Object[] { routeId }, new int[] { Types.VARCHAR });
	}

    public int add(CustomGatewayRoutes routes) {
    	 validateRoutes(routes);
    	
    	Object[] parametes = new Object[] { routes.getRouteId(), routes.getUri(), routes.getPredicates(), routes.getFilters(), routes.getOrders(),
    			routes.getDescription(), routes.getStatus(), routes.getCreateTime()};
    	
    	return jdbcTemplate.update(GATEWAY_ROUTES_INSERT_BASE, parametes);
    }

    public CustomGatewayRoutes queryByRouteId(String routeId, int flag) {
    	String sql = GATEWAY_ROUTES_QUERY_BASE + GATEWAY_ROUTES_TABLE + GATEWAY_ROUTES_QUERY_LAST_OFFLINE_ROUTE;
    	if (Objects.equal(0, flag)) {
    		sql = GATEWAY_ROUTES_QUERY_BASE + GATEWAY_ROUTES_TABLE + GATEWAY_ROUTES_QUERY_LAST_ONLINE_ROUTE;
    	}
    	return JdbcUtil.queryForObject(jdbcTemplate, sql, new Object[] { routeId }, CustomGatewayRoutes.class);
    }

    public int updateByRouteId(CustomGatewayRoutes routes) {
    	validateRoutes(routes);
    	
    	Object[] parameters = new Object[] { routes.getUri(), routes.getPredicates(), routes.getFilters(), routes.getOrders(),
    			routes.getDescription(), routes.getStatus(), routes.getRouteId() };
    	
    	return jdbcTemplate.update(GATEWAY_ROUTES_UPDATE_BASE, parameters, new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.VARCHAR });
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
    
}
