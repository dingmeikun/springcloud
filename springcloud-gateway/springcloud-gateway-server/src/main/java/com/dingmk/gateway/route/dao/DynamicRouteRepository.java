package com.dingmk.gateway.route.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dingmk.gateway.route.dto.CustomGatewayRoutes;

@Repository
public class DynamicRouteRepository {
	
	private static String deleteSQL = "delete from sys_gateway_routes where id = ?";
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public String deleteByPrimaryKey(String id) {
		
		return jdbcTemplate.queryForObject(deleteSQL, String.class);
	}

    public int insert(CustomGatewayRoutes record) {
    	return 0;
    }

    public int insertSelective(CustomGatewayRoutes record) {
    	return 0;
    }

    public CustomGatewayRoutes selectByPrimaryKey(String id) {
    	return null;
    }

    public int updateByPrimaryKeySelective(CustomGatewayRoutes record) {
    	return 0;
    }

    public int updateByPrimaryKey(CustomGatewayRoutes record) {
    	return 0;
    }

    public List<CustomGatewayRoutes> findAll(Map map){
    	return null;
    }
}
