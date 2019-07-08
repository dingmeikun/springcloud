package com.dingmk.location.baidu.bo;

import java.util.List;

import com.dingmk.location.dto.LocationRequest.Location;

import lombok.Data;

@Data
public class LocationBaidu {

	/** 状态 */
	private int status;
	
	/** 结果集 */
	private Result result;
	
	@Data
	public static class Result {
		private Location location;
		private String cityCode;
		private String business;
		private String sematic_description;
		private String formatted_address;
		private AddressBaidu addressComponent;
		private List<PoiRegions> poiRegions;
	}
	
	
}
