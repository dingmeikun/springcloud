package com.dingmk.gateway.route.model;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * Gateway自定义过滤器模型
 */
@Getter
@Setter
public class CustomFilterDefinition {

	/**
     * Filter Name
     */
    private String name;

    /**
     * 对应的路由规则
     */
    private Map<String, String> args = new LinkedHashMap<>();
}
