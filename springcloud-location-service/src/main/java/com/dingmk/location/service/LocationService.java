package com.dingmk.location.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dingmk.comm.utils.JsonMapper;
import com.dingmk.location.baidu.bo.AddressBaidu;
import com.dingmk.location.baidu.bo.LocationBaiduRet;
import com.dingmk.location.composit.CompositAPILocData;
import com.dingmk.location.dao.LocationCollectMongoDao;
import com.dingmk.location.dto.LocationRequest;
import com.dingmk.location.dto.LocationRequest.Location;
import com.dingmk.location.dto.LocationResponse;
import com.dingmk.location.gaode.bo.AddressGaode;
import com.dingmk.location.gaode.bo.LocationGaodeRet;
import com.dingmk.location.gaode.bo.LocationGaodeRet.Results.Result;
import com.dingmk.location.gaode.bo.Street;
import com.dingmk.location.util.LocationType;
import com.dingmk.location.util.LocationUtil;
import com.mongodb.CommandResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LocationService extends CompositService{

	@Resource
    private LocationCollectMongoDao repository;
	
	private static JsonMapper mapper = new JsonMapper();
	
	public LocationResponse query(LocationRequest request) {
		Location location = request.getLocation();
		String coordsys = location.getCoordsys();
		String locString = LocationUtil.getLocationStr(location);
		LocationResponse response = new LocationResponse();
		response.setCoordsys(coordsys);
		
		// 查询MongDB是否存在地理数据
		log.debug("Location Query MongoDB!");
		response = queryMongoGeolocation(location);
		if(null != response && null != response.getAddress() && null != response.getLocation()){
			return response;
		}
		
		// 查询第三方API获取地理数据
		log.debug("Location MongoDB No Data，query thirdpart API!");
		CompositAPILocData locationDto = queryThirdyAPILocation(location);
		return parseLocationResponse(locationDto, locString);
	}
	
	/**
	 * 查询MongoDB地理位置信息
	 * 
	 * @param location 经纬度信息
	 * @return 位置信息
	 */
	private LocationResponse queryMongoGeolocation(Location location) {
		// 判断经纬度信息是否为空
		String coordsys = (location.getCoordsys() == null || location.getCoordsys().isEmpty()) ? LocationType.GAODE.name() : location.getCoordsys().trim();
		String longitude = location.getLat() == null ? "" : location.getLat().trim();
		String latitude = location.getLat() == null ? "" : location.getLat().trim();
		if(longitude.isEmpty() || latitude.isEmpty()){
			return null;
		}
		Point point = new Point(Double.parseDouble(location.getLng()), Double.parseDouble(location.getLat()));
		
		LocationResponse respones = new LocationResponse();
		if(coordsys.trim().equals(LocationType.BAIDU.getName())){
			String collectionName = config.getBaiduCollection();
			CommandResult baiduResult =  queryLocationByCommand(point, collectionName, null, 1);
			LocationBaiduRet baiduRet = mapper.fromJson(baiduResult.toJson(), LocationBaiduRet.class);
			respones = parseBaiduMongoResponse(point, coordsys, baiduRet);
		}
		// 如果查询百度文档数据为空，则继续查询高德文档数据 
		boolean success = (null != respones && null != respones.getLocation() && null != respones.getAddress());
		if(!success || coordsys.trim().equals(LocationType.GAODE.getName())){
			String collectionName = config.getGaodeCollection();
			CommandResult gaodeResult =  queryLocationByCommand(point, collectionName, null, 1);
			LocationGaodeRet gaodeRet = mapper.fromJson(gaodeResult.toJson(), LocationGaodeRet.class);
			respones = parseGaodeMongoResponse(point, coordsys, gaodeRet);
		}
		
		return respones;
	}
	
	/**
	 * 查询第三方API获取地理信息
	 * 
	 * @param location 位置信息参数
	 * @return 高德/百度聚合结果
	 */
	private CompositAPILocData queryThirdyAPILocation(Location location) {
		String coordsys = (location.getCoordsys() == null || location.getCoordsys().isEmpty()) ? LocationType.GAODE.name() : location.getCoordsys().trim();
		String longitude = location.getLng() == null ? "" : location.getLng().trim();
		String latitude = location.getLat() == null ? "" : location.getLat().trim();
		if(longitude.isEmpty() || latitude.isEmpty()){
			return null;
		}
		
		CompositAPILocData locationDto = new CompositAPILocData();
		locationDto.setCoordsys(coordsys);
		try{
			locationDto = queryThirdApiDatas(longitude, latitude, LocationType.valueOf(coordsys.toUpperCase()));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return locationDto;
	}
	
	/**
	 * 获得百度MongoDB文档数据
	 * 
	 * @param point
	 * @param coordsys
	 * @param baiduRet
	 * @return
	 */
	public LocationResponse parseBaiduMongoResponse(Point point, String coordsys, LocationBaiduRet baiduRet) {
		LocationResponse respones = new LocationResponse();
		if(null != baiduRet && "1.0".equals(baiduRet.getOk()) && !CollectionUtils.isEmpty(baiduRet.getResults())){
			com.dingmk.location.baidu.bo.LocationBaiduRet.Results results = baiduRet.getResults().get(0);
			com.dingmk.location.baidu.bo.LocationBaiduRet.Results.Result result = results.getObj();
			AddressBaidu addressBaidu = result.getAddress();
			List<String> location = result.getLocation();
			LocationResponse.Address address = new LocationResponse.Address();
			address.setFmtAddress(result.getFmtAddress());
			address.setSematicDesc(result.getSematicDesc());
			address.setStreet(addressBaidu.getStreet());
			address.setLocation(location.get(0) + ", " + location.get(1));
			address.setCountry(addressBaidu.getCountry());
			address.setProvince(addressBaidu.getProvince());
			address.setCity(addressBaidu.getCity());
			address.setDistrict(addressBaidu.getDistrict());
			address.setDistance(results.getDis());
			address.setCitycode(result.getCityCode());
			address.setAdcode(addressBaidu.getAdcode());
			address.setSematicDesc(result.getSematicDesc());
			
			respones.setCoordsys(coordsys.trim());
			respones.setLocation(point.getX() + ", " + point.getY());
			respones.setAddress(address);
		}
		return respones;
	}
	
	/**
	 * 获得高德MongoDB文档数据
	 * 
	 * @param point
	 * @param coordsys
	 * @param gaodeRet
	 * @return
	 */
	public LocationResponse parseGaodeMongoResponse(Point point, String coordsys, LocationGaodeRet gaodeRet) {
		LocationResponse respones = new LocationResponse();
		if(null != gaodeRet && "1.0".equals(gaodeRet.getOk()) && !CollectionUtils.isEmpty(gaodeRet.getResults())){
			com.dingmk.location.gaode.bo.LocationGaodeRet.Results results = gaodeRet.getResults().get(0);
			Result result = results.getObj();
			AddressGaode gaodeAddress = result.getAddress();
			List<String> location = result.getLocation();
			Street street = gaodeAddress.getStreetNumber();
			
			LocationResponse.Address address = new LocationResponse.Address();
			address.setAdcode(gaodeAddress.getAdcode());
			address.setCity(gaodeAddress.getCity());
			address.setCitycode(gaodeAddress.getCitycode());
			address.setCountry(gaodeAddress.getCountry());
			address.setDistance(results.getDis());
			address.setDistrict(gaodeAddress.getDistrict());
			address.setFmtAddress(result.getFmtAddress());
			address.setLocation(location.get(0) + ", " + location.get(1));
			address.setProvince(gaodeAddress.getProvince());
			StringBuffer sematicDesc = new StringBuffer("");
			if(null != street){
				if(null != street.getStreet()){
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
				address.setStreet(street.getStreet());
				address.setSematicDesc(sematicDesc.toString());
			}
			
			respones.setCoordsys(coordsys.trim());
			respones.setLocation(point.getX() + ", " + point.getY());
			respones.setAddress(address);
		}
		return respones;
	}
	
}
