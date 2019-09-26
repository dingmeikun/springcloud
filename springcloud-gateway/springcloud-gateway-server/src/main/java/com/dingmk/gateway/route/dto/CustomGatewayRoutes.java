package com.dingmk.gateway.route.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomGatewayRoutes {

	/** 路由ID */
	private String id;
	
	/** uri路径 */
    private String uri;
    
    /** 判定器：断言 */
    private String predicates;
    
    /** 过滤器 */
    private String filters;
    
    /** 排序 */
    private Integer order;
    
    /** 描述 */
    private String description;
    
    /** 删除标志 0不删除，1删除 */
    private Integer delFlag;
    
    /** 创建时间 */
    private Date createTime;
    
    /** 更新时间 */
    private Date updateTime;
}
