package com.dingmk.gateway.route.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据库存储对象
 * 
 * @author dingmeikun
 *
 */
@Getter
@Setter
@ToString
public class CustomGatewayRoutes {
	
	/** 路由ID */
	private String routeId;
	
	/** uri路径 */
    private String uri;
    
    /** 判定器：断言 */
    private String predicates;
    
    /** 过滤器 */
    private String filters;
    
    /** 排序 */
    private Integer orders;
    
    /** 描述 */
    private String description;
    
    /** 状态:1 启用状态、0 失效状态  */
    private Integer status;
    
    /** 创建时间 */
    private String createTime;
    
    /** 更新时间 */
    private String updateTime;
    
    public boolean valide() {
    	boolean valid = true;
    	if (null == routeId || (routeId = routeId.trim()).isEmpty()) {
    		valid = false;
    	}
    	
    	if (null == uri || (uri = uri.trim()).isEmpty()) {
    		valid = false;
    	}
    	
    	if (null == status || 0 == status) {
    		valid = false;
    	}
    	
    	if (null == createTime || (createTime = createTime.trim()).isEmpty()) {
    		valid = false;
    	}
    	
    	return valid;
    }
}
