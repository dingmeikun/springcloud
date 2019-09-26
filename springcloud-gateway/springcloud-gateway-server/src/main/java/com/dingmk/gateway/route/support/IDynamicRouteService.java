package com.dingmk.gateway.route.support;

import java.util.Map;

import com.dingmk.gateway.route.dto.CustomPageResult;
import com.dingmk.gateway.route.dto.GatewayRoutesVO;
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
    String add(CustomRouteDefinition gatewayRouteDefinition);

    /**
     * 修改路由
      * @param gatewayRouteDefinition
     * @return
     */
    String update(CustomRouteDefinition gatewayRouteDefinition);



    /**
     * 删除路由
     * @param id
     * @return
     */
    String delete(String id);


    /**
     * 查询全部数据
     * @return
     */
    CustomPageResult<GatewayRoutesVO> findAll(Map<String, Object> params);

    /**
     *  同步redis数据 从mysql中同步过去
     *
     * @return
     */
    String synchronization();


    /**
     * 更改路由状态
     * @param params
     * @return
     */
    String updateFlag(Map<String, Object> params);


}
