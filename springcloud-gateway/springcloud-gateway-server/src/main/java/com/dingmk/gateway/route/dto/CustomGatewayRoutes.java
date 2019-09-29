package com.dingmk.gateway.route.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
