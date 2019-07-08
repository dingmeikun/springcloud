package com.dingmk.location.baidu.bo;

import java.util.List;

import lombok.Data;

@Data
public class LocationBaiduRes {

	/** 详细地址 */
	private AddressBaidu address;
	
	/** 经济区域 */
	private String business;
	
	/** POI区域 */
	private List<PoiRegions> poiRegions;
	
	/** 城市编码 */
	private Integer cityCode;
	
	/** 区域语义描述 */
	private String sematicDesc;
	
	/** 精简位置  */
	private String fmtAddress;
}
