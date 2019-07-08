package com.dingmk.location.baidu.bo;

import java.util.List;

import lombok.Data;

@Data
public class LocationBaiduPo {

	/** 详细地址 */
	private AddressBaidu address;
	
	/** 经济区域 */
	private String business;
	
	/** POI区域 */
	private List<PoiRegions> poiRegions;
	
	/** 城市编码 */
	private String cityCode;
	
	/** 区域语义描述 */
	private String sematicDesc;
	
	/** 地点经纬度 */
	private Double[] location;
	
	/** 合作系统 高德、百度 */
	private String coordsys;
	
	/** 精简位置  */
	private String fmtAddress;
}
