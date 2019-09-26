package com.dingmk.gateway.route.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.dingmk.gateway.route.common.BasicLogicResult;
import com.dingmk.gateway.route.dto.CustomPageResult;
import com.dingmk.gateway.route.dto.GatewayRoutesVO;
import com.dingmk.gateway.route.model.CustomFilterDefinition;
import com.dingmk.gateway.route.model.CustomPredicateDefinition;
import com.dingmk.gateway.route.model.CustomRouteDefinition;
import com.dingmk.gateway.route.support.DynamicRouteService;


@RestController
@RequestMapping("/route")
public class DynamicRouteController {

	@Autowired
    private DynamicRouteService dynamicRouteService;
	
	/**
     * 增加路由
     * @param gwdefinition
     * @return
     */
    @PostMapping("/add")
    public BasicLogicResult<String> add(@RequestBody CustomRouteDefinition gwdefinition) {
    	String route_id = dynamicRouteService.add(gwdefinition);
    	return new BasicLogicResult<String>(1, "add id OK", route_id);
    }
    
    @PostMapping("/update")
    public BasicLogicResult<String> update(@RequestBody CustomRouteDefinition definition) {
        String route_id = dynamicRouteService.update(definition);
        return new BasicLogicResult<String>(1, "update id OK", route_id);
    }

    @GetMapping("/delete/{id}")
    public BasicLogicResult<String> delete(@PathVariable String id) {
        String route_id = dynamicRouteService.delete(id);
        return new BasicLogicResult<String>(1, "delete id OK", route_id);
    }
    
    //获取全部数据
    @GetMapping("/findAll")
    public CustomPageResult<GatewayRoutesVO> findAll(@RequestParam Map<String, Object> params){
        return dynamicRouteService.findAll(params);
    }

    //同步redis数据 从mysql中同步过去
    @GetMapping("/synchronization")
    public BasicLogicResult<String> synchronization() {
    	String status = dynamicRouteService.synchronization();
    	return new BasicLogicResult<String>(1, "sync id OK", status);
    }


    //修改路由状态
    @GetMapping("/updateFlag")
    public BasicLogicResult<String> updateFlag(@RequestParam Map<String, Object> params) {
    	String status = dynamicRouteService.updateFlag(params);
    	return new BasicLogicResult<String>(1, "update Flag OK", status);
    }

    private RouteDefinition assembleRouteDefinition(CustomRouteDefinition gwdefinition) {

        RouteDefinition definition = new RouteDefinition();

        // ID
        definition.setId(gwdefinition.getId());

        // Predicates
        List<PredicateDefinition> pdList = new ArrayList<>();
        for (CustomPredicateDefinition gpDefinition: gwdefinition.getPredicates()) {
            PredicateDefinition predicate = new PredicateDefinition();
            predicate.setArgs(gpDefinition.getArgs());
            predicate.setName(gpDefinition.getName());
            pdList.add(predicate);
        }
        definition.setPredicates(pdList);

        // Filters
        List<FilterDefinition> fdList = new ArrayList<>();
        for (CustomFilterDefinition gfDefinition: gwdefinition.getFilters()) {
            FilterDefinition filter = new FilterDefinition();
            filter.setArgs(gfDefinition.getArgs());
            filter.setName(gfDefinition.getName());
            fdList.add(filter);
        }
        definition.setFilters(fdList);

        // URI
        URI uri = UriComponentsBuilder.fromUriString(gwdefinition.getUri()).build().toUri();
        definition.setUri(uri);

        return definition;
    }
}
