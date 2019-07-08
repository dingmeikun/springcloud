package com.dingmk.location.util;

import com.dingmk.location.dto.LocationRequest.Location;

public class LocationUtil {

	/**
	 * 获得location串
	 * @param location
	 * @return
	 */
	public static String getLocationStr(Location location) {
		if(null == location || null == location.getLng() || null == location.getLat()) {
			return null;
		}
		
		String lng = location.getLng();
		String lat = location.getLat();
		return lng + ", " + lat;
	}
	
	/**
	 * 位置信息转为Double数组
	 * @param location
	 * @param defautloc
	 * @return
	 */
	public static Double[] querylocationInfo(String location, String defautloc) {
		try{
			String[] loc = location.split(",");
			return new Double[] { Double.parseDouble(loc[0]),
					Double.parseDouble(loc[1])};
		}catch(Exception e){
			String[] loc = defautloc.split(",");
			return new Double[] { Double.parseDouble(loc[0]),
					Double.parseDouble(loc[1])};
		}
	}
}
