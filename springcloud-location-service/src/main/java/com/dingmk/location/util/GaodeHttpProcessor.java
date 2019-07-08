package com.dingmk.location.util;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Maps;
import com.dingmk.location.gaode.bo.AddressGaode;
import com.dingmk.location.gaode.bo.Building;
import com.dingmk.location.gaode.bo.Business;
import com.dingmk.location.gaode.bo.LocationGaode;
import com.dingmk.location.gaode.bo.Neighbor;
import com.dingmk.location.gaode.bo.Street;
import com.dingmk.location.gaode.bo.LocationGaode.Regeocode;

/**
 * 高德地理位置识别接口
 * @author Tony.Lau
 * @createTime 2017-10-18
 */
@Slf4j
public class GaodeHttpProcessor {
	
	private static final String url = "http://restapi.amap.com/v3/geocode/regeo?key=93077ec5f581f504745efd24aab466c0&location=%s,%s&output=JSON&extensions=base";
	private static JsonMapper mapper = new JsonMapper();

	public static HttpRequestBase buildRequest(String lng, String lat) {
		return new HttpGet(String.format(url, lng, lat));
	}

	@SuppressWarnings("unchecked")
	public static LocationGaode parseResponse(String respJson) {
		log.debug("gaode respJson:" + respJson);
		LocationGaode location = new LocationGaode();
		GaodeResp resp = mapper.fromJsonBytes(respJson.getBytes(StandardCharsets.UTF_8), GaodeResp.class);
		location.setStatus(resp.getStatus());
		if (null != resp && resp.getStatus() == 1) {
			Regeocode regeocode = new Regeocode();
			AddressGaode address = new AddressGaode();
			Map<String, Object> addressComponent = (Map<String, Object>) resp.getRegeocode().get("addressComponent");
			
			Object countryObj = addressComponent.get("country");
			if (null != countryObj) {
				if (String.class.isInstance(countryObj)) {
					String country = (String) countryObj;
					if (StringUtils.isNotBlank(country)) {
						address.setCountry(country);
					}
				} else if (ArrayList.class.isInstance(countryObj)) {
					ArrayList<String> countryList = (ArrayList<String>) countryObj;
					if (!CollectionUtils.isEmpty(countryList)) {
						String country = countryList.get(0);
						if (StringUtils.isNotBlank(country)) {
							address.setCountry(country);
						}
					}
				}
			}
			
			Object provinceObj = addressComponent.get("province");
			if (null != provinceObj) {
				if (String.class.isInstance(provinceObj)) {
					String province = (String) provinceObj;
					if (StringUtils.isNotBlank(province)) {
						address.setProvince(province);
					}
				} else if (ArrayList.class.isInstance(provinceObj)) {
					ArrayList<String> provinceList = (ArrayList<String>) provinceObj;
					if (!CollectionUtils.isEmpty(provinceList)) {
						String province = provinceList.get(0);
						if (StringUtils.isNotBlank(province)) {
							address.setProvince(province);
						}
					}
				}
			}
			
			Object cityObj = addressComponent.get("city");
			if (null != cityObj) {
				if (String.class.isInstance(cityObj)) {
					String city = (String) cityObj;
					if (StringUtils.isNotBlank(city)) {
						address.setCity(city);
					}
				} else if (ArrayList.class.isInstance(cityObj)) {
					ArrayList<String> cityList = (ArrayList<String>) cityObj;
					if (!CollectionUtils.isEmpty(cityList)) {
						String city = cityList.get(0);
						if (StringUtils.isNotBlank(city)) {
							address.setCity(city);
						}
					}
				}
			}

			Object districtObj = addressComponent.get("district");
			if (null != districtObj) {
				if (String.class.isInstance(districtObj)) {
					String district = (String) districtObj;
					if (StringUtils.isNotBlank(district)) {
						address.setDistrict(district);
					}
				}
				if (ArrayList.class.isInstance(districtObj)) {
					ArrayList<String> districtList = (ArrayList<String>) districtObj;
					if (!CollectionUtils.isEmpty(districtList)) {
						String district = districtList.get(0);
						if (StringUtils.isNotBlank(district)) {
							address.setDistrict(district);
						}
					}
				}
			}
			
			Object citycodeObj = addressComponent.get("citycode");
			if (null != citycodeObj) {
				if (String.class.isInstance(citycodeObj)) {
					String citycode = (String) citycodeObj;
					if (StringUtils.isNotBlank(citycode)) {
						address.setCitycode(citycode);
					}
				} else if (ArrayList.class.isInstance(citycodeObj)) {
					ArrayList<String> citycodeList = (ArrayList<String>) citycodeObj;
					if (!CollectionUtils.isEmpty(citycodeList)) {
						String citycode = citycodeList.get(0);
						if (StringUtils.isNotBlank(citycode)) {
							address.setCitycode(citycode);
						}
					}
				}
			}
			
			Object seaAreaObj = addressComponent.get("seaArea");
			if (null != seaAreaObj) {
				if (String.class.isInstance(seaAreaObj)) {
					String seaArea = (String) seaAreaObj;
					if (StringUtils.isNotBlank(seaArea)) {
						address.setSeaArea(seaArea);
					}
				} else if (ArrayList.class.isInstance(seaAreaObj)) {
					ArrayList<String> seaAreaList = (ArrayList<String>) seaAreaObj;
					if (!CollectionUtils.isEmpty(seaAreaList)) {
						String seaArea = seaAreaList.get(0);
						if (StringUtils.isNotBlank(seaArea)) {
							address.setSeaArea(seaArea);
						}
					}
				}
			}
			
			Object townshipObj = addressComponent.get("township");
			if (null != townshipObj) {
				if (String.class.isInstance(townshipObj)) {
					String township = (String) townshipObj;
					if (StringUtils.isNotBlank(township)) {
						address.setTownship(township);
					}
				} else if (ArrayList.class.isInstance(townshipObj)) {
					ArrayList<String> townshipList = (ArrayList<String>) townshipObj;
					if (!CollectionUtils.isEmpty(townshipList)) {
						String township = townshipList.get(0);
						if (StringUtils.isNotBlank(township)) {
							address.setTownship(township);
						}
					}
				}
			}
			
			Object towncodeObj = addressComponent.get("towncode");
			if (null != towncodeObj) {
				if (String.class.isInstance(towncodeObj)) {
					String towncode = (String) towncodeObj;
					if (StringUtils.isNotBlank(towncode)) {
						address.setTowncode(towncode);
					}
				} else if (ArrayList.class.isInstance(towncodeObj)) {
					ArrayList<String> towncodeList = (ArrayList<String>) towncodeObj;
					if (!CollectionUtils.isEmpty(towncodeList)) {
						String towncode = towncodeList.get(0);
						if (StringUtils.isNotBlank(towncode)) {
							address.setTowncode(towncode);
						}
					}
				}
			}
			
			Object adcodeObj = addressComponent.get("adcode");
			if (null != adcodeObj) {
				if (String.class.isInstance(adcodeObj)) {
					String adcode = (String) adcodeObj;
					if (StringUtils.isNotBlank(adcode)) {
						address.setAdcode(adcode);
					}
				} else if (ArrayList.class.isInstance(adcodeObj)) {
					ArrayList<String> adcodeList = (ArrayList<String>) adcodeObj;
					if (!CollectionUtils.isEmpty(adcodeList)) {
						String adcode = adcodeList.get(0);
						if (StringUtils.isNotBlank(adcode)) {
							address.setAdcode(adcode);
						}
					}
				}
			}
			
			/** 获取businessAreas */
			List<Business> businessAreas = new ArrayList<Business>();
			ArrayList<Object> businessList = (ArrayList<Object>) addressComponent.get("businessAreas");
			if (!CollectionUtils.isEmpty(businessList)) {
				for(Object business : businessList){
					if(Map.class.isInstance(business)){
						Map<String, String> mapObj = (Map<String, String>) business;
						if(!CollectionUtils.isEmpty(mapObj)){
							Business busiObj = new Business();
							String loc = mapObj.get("location");
							if (StringUtils.isNotBlank(loc)) {
								busiObj.setLocation(loc);
							}
							String name = mapObj.get("name");
							if (StringUtils.isNotBlank(name)) {
								busiObj.setName(name);
							}
							String id = mapObj.get("id");
							if (StringUtils.isNotBlank(id)) {
								busiObj.setId(id);
							}
							businessAreas.add(busiObj);
						}
					}else if (ArrayList.class.isInstance(business)) {
						ArrayList<Map<String, String>> listObj = (ArrayList<Map<String, String>>) business;
						if(!CollectionUtils.isEmpty(listObj)){
							Map<String, String> mapObj = listObj.get(0);
							if(!CollectionUtils.isEmpty(mapObj)){
								Business busiObj = new Business();
								String loc = mapObj.get("location");
								if (StringUtils.isNotBlank(loc)) {
									busiObj.setLocation(loc);
								}
								String name = mapObj.get("name");
								if (StringUtils.isNotBlank(name)) {
									busiObj.setName(name);
								}
								String id = mapObj.get("id");
								if (StringUtils.isNotBlank(id)) {
									busiObj.setId(id);
								}
								businessAreas.add(busiObj);
							}
						}
					}
				}
			}
			address.setBusinessAreas(businessAreas);
			
			/** 获取Neighbor */
			Neighbor neighbor = new Neighbor();
			Map<String, Object> neighborhood = (Map<String, Object>) addressComponent.get("neighborhood");
			if(neighborhood != null && !CollectionUtils.isEmpty(neighborhood)){
				Object nnameObj = neighborhood.get("name");
				if (null != nnameObj) {
					if (String.class.isInstance(nnameObj)) {
						String name = (String) nnameObj;
						if (StringUtils.isNotBlank(name)) {
							neighbor.setName(name);
						}
					} else if (ArrayList.class.isInstance(nnameObj)) {
						ArrayList<String> nameList = (ArrayList<String>) nnameObj;
						if (!CollectionUtils.isEmpty(nameList)) {
							String name = nameList.get(0);
							if (StringUtils.isNotBlank(name)) {
								neighbor.setName(name);
							}
						}
					}
				}
				Object ntypeObj = neighborhood.get("type");
				if (null != ntypeObj) {
					if (String.class.isInstance(ntypeObj)) {
						String type = (String) ntypeObj;
						if (StringUtils.isNotBlank(type)) {
							neighbor.setType(type);
						}
					} else if (ArrayList.class.isInstance(ntypeObj)) {
						ArrayList<String> typeList = (ArrayList<String>) ntypeObj;
						if (!CollectionUtils.isEmpty(typeList)) {
							String type = typeList.get(0);
							if (StringUtils.isNotBlank(type)) {
								neighbor.setType(type);
							}
						}
					}
				}
			}
			address.setNeighborhood(neighbor);
			
			/** 获取building */
			Building buildings = new Building();
			Map<String, Object> building = (Map<String, Object>) addressComponent.get("building");
			if(building != null && !CollectionUtils.isEmpty(building)){
				Object bnameObj = building.get("name");
				if (null != bnameObj) {
					if (String.class.isInstance(bnameObj)) {
						String name = (String) bnameObj;
						if (StringUtils.isNotBlank(name)) {
							buildings.setName(name);
						}
					} else if (ArrayList.class.isInstance(bnameObj)) {
						ArrayList<String> nameList = (ArrayList<String>) bnameObj;
						if (!CollectionUtils.isEmpty(nameList)) {
							String name = nameList.get(0);
							if (StringUtils.isNotBlank(name)) {
								buildings.setName(name);
							}
						}
					}
				}
				Object btypeObj = building.get("type");
				if (null != btypeObj) {
					if (String.class.isInstance(btypeObj)) {
						String type = (String) btypeObj;
						if (StringUtils.isNotBlank(type)) {
							buildings.setType(type);
						}
					} else if (ArrayList.class.isInstance(btypeObj)) {
						ArrayList<String> typeList = (ArrayList<String>) btypeObj;
						if (!CollectionUtils.isEmpty(typeList)) {
							String type = typeList.get(0);
							if (StringUtils.isNotBlank(type)) {
								buildings.setType(type);
							}
						}
					}
				}
			}
			address.setBuilding(buildings);
			
			/** 获取streetNumber */
			Street streetNum = new Street();
			Map<String, Object> streetNumber = (Map<String, Object>) addressComponent.get("streetNumber");
			if(streetNumber != null && !CollectionUtils.isEmpty(streetNumber)){
				Object numberObj = streetNumber.get("number");
				if (null != numberObj) {
					if (String.class.isInstance(numberObj)) {
						String number = (String) numberObj;
						if (StringUtils.isNotBlank(number)) {
							streetNum.setNumber(number);
						}
					} else if (ArrayList.class.isInstance(numberObj)) {
						ArrayList<String> numberList = (ArrayList<String>) numberObj;
						if (!CollectionUtils.isEmpty(numberList)) {
							String number = numberList.get(0);
							if (StringUtils.isNotBlank(number)) {
								streetNum.setNumber(number);
							}
						}
					}
				}
				Object locationObj = streetNumber.get("location");
				if (null != locationObj) {
					if (String.class.isInstance(locationObj)) {
						String locations = (String) locationObj;
						if (StringUtils.isNotBlank(locations)) {
							streetNum.setLocation(locations);
						}
					} else if (ArrayList.class.isInstance(locationObj)) {
						ArrayList<String> locationList = (ArrayList<String>) locationObj;
						if (!CollectionUtils.isEmpty(locationList)) {
							String locations = locationList.get(0);
							if (StringUtils.isNotBlank(locations)) {
								streetNum.setLocation(locations);
							}
						}
					}
				}
				Object directionObj = streetNumber.get("direction");
				if (null != directionObj) {
					if (String.class.isInstance(directionObj)) {
						String direction = (String) directionObj;
						if (StringUtils.isNotBlank(direction)) {
							streetNum.setDirection(direction);
						}
					} else if (ArrayList.class.isInstance(directionObj)) {
						ArrayList<String> directionList = (ArrayList<String>) directionObj;
						if (!CollectionUtils.isEmpty(directionList)) {
							String direction = directionList.get(0);
							if (StringUtils.isNotBlank(direction)) {
								streetNum.setDirection(direction);
							}
						}
					}
				}
				Object streetObj = streetNumber.get("street");
				if (null != streetObj) {
					if (String.class.isInstance(streetObj)) {
						String street = (String) streetObj;
						if (StringUtils.isNotBlank(street)) {
							streetNum.setStreet(street);
						}
					} else if (ArrayList.class.isInstance(streetObj)) {
						ArrayList<String> streetList = (ArrayList<String>) streetObj;
						if (!CollectionUtils.isEmpty(streetList)) {
							String street = streetList.get(0);
							if (StringUtils.isNotBlank(street)) {
								streetNum.setStreet(street);
							}
						}
					}
				}
				Object distanceObj = streetNumber.get("distance");
				if (null != distanceObj) {
					if (String.class.isInstance(distanceObj)) {
						String distance = (String) distanceObj;
						if (StringUtils.isNotBlank(distance)) {
							streetNum.setDistance(distance);
						}
					} else if (ArrayList.class.isInstance(distanceObj)) {
						ArrayList<String> distanceList = (ArrayList<String>) distanceObj;
						if (!CollectionUtils.isEmpty(distanceList)) {
							String distance = distanceList.get(0);
							if (StringUtils.isNotBlank(distance)) {
								streetNum.setDistance(distance);
							}
						}
					}
				}
			}
			address.setStreetNumber(streetNum);
			regeocode.setAddressComponent(address);
			
			/** 获取formatted_address */
			Object formatAddress = resp.getRegeocode().get("formatted_address");
			if (null != formatAddress) {
				if (String.class.isInstance(formatAddress)) {
					String fomataddress = (String) formatAddress;
					if (StringUtils.isNotBlank(fomataddress)) {
						regeocode.setFormatted_address(formatAddress.toString());
					}
				} else if (ArrayList.class.isInstance(formatAddress)) {
					ArrayList<String> addressList = (ArrayList<String>) formatAddress;
					if (!CollectionUtils.isEmpty(addressList)) {
						String fomataddress = addressList.get(0);
						if (StringUtils.isNotBlank(fomataddress)) {
							regeocode.setFormatted_address(formatAddress.toString());
						}
					}
				}
			}
			location.setRegeocode(regeocode);
		}
		return location;
	}

	static class GaodeResp {
		private int status;
		private Map<String, Object> regeocode = Maps.newHashMap();

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public Map<String, Object> getRegeocode() {
			return regeocode;
		}

		public void setRegeocode(Map<String, Object> regeocode) {
			this.regeocode = regeocode;
		}
	}
}
