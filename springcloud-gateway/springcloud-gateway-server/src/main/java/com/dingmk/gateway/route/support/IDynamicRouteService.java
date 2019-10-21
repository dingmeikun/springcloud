package com.dingmk.gateway.route.support;

import com.dingmk.gateway.route.common.BasicLogicResult;
import com.dingmk.gateway.route.model.CustomRouteDefinition;

/**
 * 操作 路由 Service
 */
public interface IDynamicRouteService {

    /**
     * 新增路由
     * @param gatewayRouteDefinition
     * @return
     */
	BasicLogicResult<String> add(CustomRouteDefinition gatewayRouteDefinition);

    /**
     * 修改路由
      * @param gatewayRouteDefinition
     * @return
     */
	BasicLogicResult<String> update(CustomRouteDefinition gatewayRouteDefinition);

    /**
     * 删除路由
     * @param id
     * @return
     */
	BasicLogicResult<String> delete(String id);

    /**
     *  同步redis数据 从mysql中同步过去
     *
     * @return
     */
	BasicLogicResult<String> synchronization();
	
	/**
     * 上线路由
     * @param
     * @return
     */
	BasicLogicResult<String> online(String routeId, int status);
	
	/**
     * 下线路由
     * @param
     * @return
     */
	BasicLogicResult<String> offline(String routeId, int status);
}
