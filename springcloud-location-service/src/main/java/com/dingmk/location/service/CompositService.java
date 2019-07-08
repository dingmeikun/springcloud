package com.dingmk.location.service;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.client.methods.HttpRequestBase;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import com.dingmk.httptemplate.support.model.HttpTemplate;
import com.dingmk.location.baidu.bo.AddressBaidu;
import com.dingmk.location.baidu.bo.LocationBaidu;
import com.dingmk.location.baidu.bo.LocationBaidu.Result;
import com.dingmk.location.baidu.bo.LocationBaiduPo;
import com.dingmk.location.composit.CompositAPILocData;
import com.dingmk.location.config.GlobalConfigService;
import com.dingmk.location.config.MongoDBConifg;
import com.dingmk.location.dao.LocationCollectMongoDao;
import com.dingmk.location.dto.LocationRequest.Location;
import com.dingmk.location.dto.LocationResponse;
import com.dingmk.location.gaode.bo.AddressGaode;
import com.dingmk.location.gaode.bo.LocationGaode;
import com.dingmk.location.gaode.bo.LocationGaode.Regeocode;
import com.dingmk.location.gaode.bo.LocationGaodePo;
import com.dingmk.location.gaode.bo.Street;
import com.dingmk.location.util.BaiduHttpProcessor;
import com.dingmk.location.util.GaodeHttpProcessor;
import com.dingmk.location.util.LocationType;
import com.dingmk.location.util.LocationUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Service
@Slf4j
public class CompositService {
	
	@Resource
	public MongoDBConifg config;
	
	@Resource
    private HttpTemplate httpTemplate;
	
	@Resource
	private LocationCollectMongoDao repository;
	
	@Resource
    private GlobalConfigService globalConfigService;
	
	/**
	 * 保存地理位置信息
	 * 
	 * @param po
	 */
	public void saveLocationInfo(Object po, String collectionName){
        if(null != po && null != collectionName){
        	repository.save(po, collectionName);
        }
    }
	
	/**
	 * 按照Command命令查询
	 * 
	 * @param point 中心坐标点
	 * @param collectionName 文档名称
	 * @param query 查询条件
	 * @param limit 限制个数
	 * @param maxDistance 最大距离，扩展参数minDistance 最小距离
	 * @return 文档结果集
	 */
	@SuppressWarnings("deprecation")
	public CommandResult queryLocationByCommand(Point point, String collectionName, DBObject query, int limit) {
		if(query==null){
			query = new BasicDBObject(); /** 增加条件 query.put("name", "zhangsan"); */
		}
		
		DB db = null;
		DBCursor dbCursor = null;
		Long maxDistance = config.getMaxDistance();
		try {
			db = config.getClient().getDB(config.getMongoDatabase().getName());
			BasicDBObject dBObject = new BasicDBObject();
			dBObject.append("geoNear", collectionName);
			dBObject.append("near", new Double[]{ point.getX(), point.getY() });
			dBObject.append("spherical", true);
			dBObject.append("distanceMultiplier", 6378137.0); /** 地球的半径，单位米*/
			dBObject.append("maxDistance", (double) maxDistance / 6378137);
			dBObject.append("query", query);
			dBObject.append("limit", limit);
			CommandResult result = db.command(dBObject);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != dbCursor) {
				dbCursor.close();
			}
		}
		return null;
	}

	/**
	 * 获取位置响应消息(第三方API)
	 * 
	 * @param locationData
	 * @return
	 */
	public LocationResponse parseLocationResponse(CompositAPILocData locationData, String locString){
		LocationResponse response = new LocationResponse();
		String coordsys = locationData.getCoordsys();
		if(LocationType.GAODE.getName().equals(coordsys)){
			LocationGaode gaodeData = locationData.getLocationGaode();
			if(null != gaodeData && 1 == gaodeData.getStatus() && null != gaodeData.getRegeocode()){
				Regeocode regeocode = gaodeData.getRegeocode();
				String collectionName = config.getGaodeCollection();
				response = parseAndSaveGaodeResponse(regeocode, coordsys, locString, collectionName);
			}
		}else if(LocationType.BAIDU.getName().equals(coordsys)){
			LocationBaidu baiduData = locationData.getLocationBaidu();
			if(null != baiduData && 0 == baiduData.getStatus() && null != baiduData.getResult()){
				Result result = baiduData.getResult();
				String collectionName = config.getBaiduCollection();
				response = parseAndSaveBaiduResponse(result, coordsys, locString, collectionName);
			}
		}
		return response;
	}
	
	/**
	 * 解析响应数据并入库高德数据
	 * @param regeocode
	 * @param coordsys
	 * @param locString
	 * @return
	 */
	public LocationResponse parseAndSaveGaodeResponse(Regeocode regeocode, String coordsys, String locString, String collectionName){
		LocationResponse response = new LocationResponse();
		if(null != regeocode.getAddressComponent()){
			LocationResponse.Address address = new LocationResponse.Address();
			AddressGaode gaodeAddress = regeocode.getAddressComponent();
			Street street = gaodeAddress.getStreetNumber();
			address.setCountry(gaodeAddress.getCountry());
			address.setProvince(gaodeAddress.getProvince());
			address.setCity(gaodeAddress.getCity());
			address.setCitycode(gaodeAddress.getCitycode());
			address.setAdcode(gaodeAddress.getAdcode());
			address.setDistrict(gaodeAddress.getDistrict());
			address.setFmtAddress(regeocode.getFormatted_address());
			StringBuffer sematicDesc = new StringBuffer("");
			if(null != street){
				if(null != street.getStreet()){
					address.setStreet(street.getStreet());
					sematicDesc.append(street.getStreet());
				}
				if(null != street.getDirection()){
					sematicDesc.append(street.getDirection());
				}
				if(null != street.getDistance()){
					sematicDesc.append(street.getDistance()).append("米");
				}
				if(null != street.getNumber()){
					sematicDesc.append(street.getNumber());
				}
				address.setLocation(street.getLocation());
			}
			address.setSematicDesc(sematicDesc.toString());
			address.setDistance(0.0);// API查询，默认值为0
			
			response.setLocation(locString);
			response.setCoordsys(coordsys);
			response.setAddress(address);
		}
		
		/** 数据入库 */
		log.debug("Location Query GAODE API success! saving data ...");
		LocationGaodePo locationGaodePo = new LocationGaodePo();
		AddressGaode address = regeocode.getAddressComponent();
		String location = address.getStreetNumber().getLocation();
		locationGaodePo.setAddress(address);
		locationGaodePo.setCoordsys(coordsys);
		locationGaodePo.setFmtAddress(regeocode.getFormatted_address());
		locationGaodePo.setLocation(LocationUtil.querylocationInfo(location, locString));
		
		saveLocationInfo(locationGaodePo, collectionName);
		log.debug("Save data success! dataset {}", locationGaodePo.toString());
		return response;
	}
	
	/**
	 * 解析响应数据并入库百度数据
	 * @param result
	 * @param coordsys
	 * @param locString
	 * @return
	 */
	public LocationResponse parseAndSaveBaiduResponse(Result result, String coordsys, String locString, String collectionName){
		LocationResponse response = new LocationResponse();
		if(null != result.getAddressComponent()){
			LocationResponse.Address address = new LocationResponse.Address();
			AddressBaidu baiduAddress = result.getAddressComponent();
			Location location = result.getLocation();
			address.setDistance(0.0);
			address.setCountry(baiduAddress.getCountry());
			address.setProvince(baiduAddress.getProvince());
			address.setCity(baiduAddress.getCity());
			address.setCitycode(result.getCityCode());
			address.setAdcode(baiduAddress.getAdcode());
			address.setDistrict(baiduAddress.getDistrict());
			address.setStreet(baiduAddress.getStreet());
			address.setFmtAddress(result.getFormatted_address());
			address.setSematicDesc(result.getSematic_description());
			address.setLocation(location.getLng() + ", " + location.getLat());
			
			response.setCoordsys(coordsys);
			response.setLocation(locString);
			response.setAddress(address);
		}
		
		/** 数据入库 */
		log.debug("Location Query BAIDU API success! saving data ...");
		LocationBaiduPo locationBaiduPo = new LocationBaiduPo();
		Location location = result.getLocation();
		String disLoc = location.getLng() + "," + location.getLat();
		locationBaiduPo.setAddress(result.getAddressComponent());
		locationBaiduPo.setBusiness(result.getBusiness());
		locationBaiduPo.setCityCode(result.getCityCode());
		locationBaiduPo.setCoordsys(coordsys);
		locationBaiduPo.setFmtAddress(result.getFormatted_address());
		locationBaiduPo.setLocation(LocationUtil.querylocationInfo(disLoc, locString));
		locationBaiduPo.setPoiRegions(result.getPoiRegions());
		locationBaiduPo.setSematicDesc(result.getSematic_description());
		
		saveLocationInfo(locationBaiduPo, collectionName);
		log.debug("Save data success! dataset {}", locationBaiduPo.toString());
		return response;
	}
	
	/**
	 * 调用三方API并返回结果
	 * @param lng
	 * @param lat
	 * @param locType
	 * @return
	 */
	public CompositAPILocData queryThirdApiDatas(String lng, String lat, LocationType locType){
		CompositAPILocData locationDto = new CompositAPILocData();
		locationDto.setCoordsys(locType.getName());
		switch(locType){
		case GAODE:
			if(globalConfigService.getGaodeLimiter().tryAcquire()){
				LocationGaode locationGaoDe = invokeThirdApiURL(GaodeHttpProcessor.buildRequest(lng, lat), LocationType.GAODE.name());
				locationDto.setLocationGaode(locationGaoDe);
			}
			break;
		case BAIDU:
			if(globalConfigService.getBaiduLimiter().tryAcquire()){
				LocationBaidu locationBaiDu = invokeThirdApiURL(BaiduHttpProcessor.buildRequest(lat, lng), LocationType.BAIDU.name());
				locationDto.setLocationBaidu(locationBaiDu);
			}
			break;
		default: // 默认高德
			if(globalConfigService.getGaodeLimiter().tryAcquire()){
				LocationGaode locationdefault = invokeThirdApiURL(GaodeHttpProcessor.buildRequest(lng, lat), LocationType.GAODE.name());
				locationDto.setLocationGaode(locationdefault);
				locationDto.setCoordsys(LocationType.GAODE.getName());
			}
			break;
		}
		return locationDto;
	}
	
	/**
	 * 调用第三方API
	 * @param uri
	 * @param coordsys
	 * @return
	 */
	public <T> T invokeThirdApiURL(HttpRequestBase httpRequestBase, String coordsys){
		if(log.isDebugEnabled()){
			log.debug("地理位置调用渠道:{}, 请求内容:{}", coordsys, httpRequestBase.getURI().toString());
		}
		
        try {
        	String respJson = httpTemplate.getStringResponse(LocationType.valueOf(coordsys), httpRequestBase);
            return parseResponse(respJson, coordsys);
        } catch (Exception e) {
            log.error("调用渠道:" + coordsys + ",调用第三方API异常 {}", e.getMessage(), e);
        }
		return null;
	}
	
	/**
	 * 解析三方返回的结果
	 * @param respJson
	 * @param coordsys
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parseResponse(String respJson, String coordsys){
		if(LocationType.GAODE.name().equals(coordsys)){
			return (T) GaodeHttpProcessor.parseResponse(respJson);
		}else if(LocationType.BAIDU.name().equals(coordsys)){
			return (T) BaiduHttpProcessor.parseResponse(respJson);
		}
		return (T) BaiduHttpProcessor.parseResponse(respJson);
	}
}
