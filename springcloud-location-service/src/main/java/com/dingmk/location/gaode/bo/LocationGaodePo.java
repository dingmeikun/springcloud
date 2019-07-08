package com.dingmk.location.gaode.bo;

import lombok.Data;

@Data
public class LocationGaodePo {

	/** 详细地址 */
	private AddressGaode address;
	
	/** 地点经纬度 */
	private Double[] location;
	
	/** 合作系统 高德、百度 */
	private String coordsys;
	
	/** 精简位置  */
	private String fmtAddress;
}
