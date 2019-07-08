package com.dingmk.location.util;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.springframework.util.CollectionUtils;

import com.dingmk.location.baidu.bo.AddressBaidu;
import com.dingmk.location.baidu.bo.LocationBaidu;
import com.dingmk.location.baidu.bo.LocationBaidu.Result;
import com.dingmk.location.baidu.bo.PoiRegions;
import com.dingmk.location.dto.LocationRequest.Location;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * 百度地理位置识别接口
 * @author Tony.Lau
 * @createTime 2017-10-08
 */
@Slf4j
public class BaiduHttpProcessor {
	
    private static final String url = "http://api.map.baidu.com/geocoder/v2/?ak=6a0ddfcfdf1a1e7a1f38501fc5d218bf&location=%s,%s&output=json&pois=0";
    private static JsonMapper mapper = new JsonMapper();

    public static HttpRequestBase buildRequest(String lat, String lng) {
        return new HttpGet(String.format(url, lat, lng));
    }

    @SuppressWarnings("unchecked")
    public static LocationBaidu parseResponse(String respJson) {
    	log.debug("baidu respJson:" + respJson);
    	LocationBaidu locationBaidu = new LocationBaidu();
        BaiduResp resp = mapper.fromJsonBytes(respJson.getBytes(StandardCharsets.UTF_8), BaiduResp.class);
        locationBaidu.setStatus(resp.getStatus());
        if (null != resp && resp.getStatus() == 0) {
        	
        	/** 获取Result结果 */
        	Result result = new Result();
        	Object formatAddress = resp.getResult().get("formatted_address");
        	if(null != formatAddress){
        		String fomataddress = (String) formatAddress;
				if (StringUtils.isNotBlank(fomataddress)) {
					result.setFormatted_address(fomataddress);
				}
        	}
        	
        	Object business = resp.getResult().get("business");
        	if(null != business){
        		String busiaddress = (String) business;
				if (StringUtils.isNotBlank(busiaddress)) {
					result.setBusiness(busiaddress);
				}
        	}
        	
        	Object description = resp.getResult().get("sematic_description");
        	if(null != description){
        		String desc = (String) description;
				if (StringUtils.isNotBlank(desc)) {
					result.setSematic_description(desc);
				}
        	}
        	
        	Object cityCode = resp.getResult().get("cityCode");
        	if(null != cityCode){
        		Integer code = (Integer) cityCode;
				result.setCityCode(String.valueOf(code));
        	}
        	
        	/** 获取location */
        	Location location = new Location();
        	Map<String, Double> locations = (Map<String, Double>) resp.getResult().get("location");
        	Double lng = locations.get("lng");
        	Double lat = locations.get("lat");
        	if (null != lng) {
        		location.setLng(String.valueOf(lng));
            }
        	if (null != lat) {
        		location.setLat(String.valueOf(lat));
            }
        	location.setCoordsys("baidu");
        	result.setLocation(location);
        	
        	/** 获取poiRegions */
        	List<PoiRegions> poiRegions = new ArrayList<PoiRegions>();
        	ArrayList<Map<String, String>> poiRegionObj = (ArrayList<Map<String, String>>) resp.getResult().get("poiRegions");
        	if (!CollectionUtils.isEmpty(poiRegionObj)) {
        		for(Map<String, String> obj : poiRegionObj){
        			PoiRegions poiRegion = new PoiRegions();
        			String name = obj.get("name");
        			if (StringUtils.isNotBlank(name)) {
        				poiRegion.setName(name);
                    }
        			String tag = obj.get("tag");
        			if (StringUtils.isNotBlank(tag)) {
        				poiRegion.setTag(tag);
                    }
        			String direction_desc = obj.get("direction_desc");
        			if (StringUtils.isNotBlank(direction_desc)) {
        				poiRegion.setDirection_desc(direction_desc);
                    }
        			String uid = obj.get("uid");
        			if (StringUtils.isNotBlank(uid)) {
        				poiRegion.setUid(uid);
                    }
        			poiRegions.add(poiRegion);
        		}
			}
        	result.setPoiRegions(poiRegions);
        	
        	/** 获取addressComponent */
        	AddressBaidu address = new AddressBaidu();
			Map<String, Object> addressComponent = (Map<String, Object>) resp.getResult().get("addressComponent");
			
			String country = (String) addressComponent.get("country");
			if (StringUtils.isNotBlank(country)) {
				address.setCountry(country);
            }
			
            String city = (String) addressComponent.get("city");
            if (StringUtils.isNotBlank(city)) {
            	address.setCity(city);
            }
            
            Integer countCode = (Integer) addressComponent.get("country_code");
            if (null != countCode && StringUtils.isNotBlank(countCode.toString())) {
            	address.setCountry_code(String.valueOf(countCode));
            }
            
            String country_code_iso = (String) addressComponent.get("country_code_iso");
            if (StringUtils.isNotBlank(country_code_iso)) {
            	address.setCountry_code_iso(country_code_iso);
            }
            
            String country_code_iso2 = (String) addressComponent.get("country_code_iso2");
            if (StringUtils.isNotBlank(country_code_iso2)) {
            	address.setCountry_code_iso2(country_code_iso2);
            }
            
            String province = (String) addressComponent.get("province");
            if (StringUtils.isNotBlank(province)) {
            	address.setProvince(province);
            }
            
            String district = (String) addressComponent.get("district");
            if (StringUtils.isNotBlank(district)) {
            	address.setDistrict(district);
            }
            
            Object citylevel = addressComponent.get("city_level");
            if (null != citylevel && StringUtils.isNotBlank(citylevel.toString())) {
            	address.setCity_level((Integer) addressComponent.get("city_level"));
            }
            
            String town = (String) addressComponent.get("town");
            if (StringUtils.isNotBlank(town)) {
            	address.setTown(town);
            }
            
            String adcode = (String) addressComponent.get("adcode");
            if (StringUtils.isNotBlank(adcode)) {
            	address.setAdcode(adcode);
            }
            
            String street = (String) addressComponent.get("street");
            if (StringUtils.isNotBlank(street)) {
            	address.setStreet(street);
            }
            
            String street_number = (String) addressComponent.get("street_number");
            if (StringUtils.isNotBlank(street_number)) {
            	address.setStreet_number(street_number);
            }
            
            String direction = (String) addressComponent.get("direction");
            if (StringUtils.isNotBlank(direction)) {
            	address.setDirection(direction);
            }
            
            String distance = (String) addressComponent.get("distance");
            if (StringUtils.isNotBlank(distance)) {
            	address.setDistance(distance);
            }
            result.setAddressComponent(address);
            locationBaidu.setResult(result);
        }
        return locationBaidu;
    }

    public static class BaiduResp {
        private int status;
        private Map<String, Object> result = Maps.newHashMap();

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Map<String, Object> getResult() {
            return result;
        }

        public void setResult(Map<String, Object> result) {
            this.result = result;
        }
    }
}
