package com.dingmk.location.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.geo.Box;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.dingmk.location.gaode.bo.LocationGaodePo;

@Repository
public class LocationCollectMongoDao {
    
    @Resource
    private MongoTemplate mongoTemplate;
    
    /**
     * MongoDB存储对象信息
     * 
     * @param location MongoDB存储对象
     * @param collectionName 文档名称
     */
    public void save(Object location, String collectionName) {
        mongoTemplate.save(location, collectionName);
    }
    
    /**
     * 按条件查询
     * 
     * @param query
     * @param collectionName
     * @return
     */
    public List<LocationGaodePo> findCityInfosByCondition(Query query, String collectionName) {
        return mongoTemplate.find(query, LocationGaodePo.class, collectionName);
    }
    
    /**
     * 按圆形查找
     * maxDistance / 111 ： 111表示单位经度的实际长度maxDistance，单位(公里)
     * maxDistance/6378137 : 6378137表示地球半径长度maxDistance，计算得实际长度，单位(米)
     *
     * @param point 中心坐标
     * @param maxDistance 距离
     * @return
     */
    public List<LocationGaodePo> findCircleNear(Point point, double maxDistance) {
        Query query = new Query(Criteria.where("location").near(point).maxDistance(maxDistance / 111));
        return mongoTemplate.find(query, LocationGaodePo.class);
    }
    
    /**
     * 按方形查找
     *
     * @param lowerLeft 左下坐标
     * @param upperRight 右上坐标
     * @return
     */
    public List<LocationGaodePo> findBoxWithin(Point lowerLeft, Point upperRight) {
        Query query = new Query(Criteria.where("location").within(new Box(lowerLeft, upperRight)));
        return mongoTemplate.find(query, LocationGaodePo.class);
    }

}
