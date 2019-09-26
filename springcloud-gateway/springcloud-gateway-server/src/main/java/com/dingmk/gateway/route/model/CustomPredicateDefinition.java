package com.dingmk.gateway.route.model;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * Gateway自定义断言模型
 */
@Getter
@Setter
public class CustomPredicateDefinition {

	/**
     * 断言对应的Name
     */
    private String name;

    /**
     * 配置的断言规则
     */
    private Map<String, String> args = new LinkedHashMap<>();
}
