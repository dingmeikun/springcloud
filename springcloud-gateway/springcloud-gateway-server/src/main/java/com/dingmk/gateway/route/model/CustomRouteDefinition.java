package com.dingmk.gateway.route.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Gateway自定义路由模型
 */
@EqualsAndHashCode
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomRouteDefinition {

	/**
     * 	路由的Id
     */
    private String id;

    /**
     * 	路由断言集合配置
     */
    private List<CustomPredicateDefinition> predicates;

    /**
     *	 路由过滤器集合配置
     */
    private List<CustomFilterDefinition> filters;

    /**
     *	 路由规则转发的目标uri
     */
    private String uri;

    /**
     *	 路由执行的顺序
     */
    private int order;
    
    /**
     * 	路由描述
     */
    private String description;
}
