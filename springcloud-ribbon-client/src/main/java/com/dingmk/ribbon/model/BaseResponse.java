package com.dingmk.ribbon.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaseResponse {

	/** 经纬度（请求） */
	private String location;
	
	/** 合作系统 高德、百度 */
	private String coordsys;
	
	/** 详细地址 */
	private Address address;
	
	@Getter
	@Setter
	@ToString
	public static class Address{
		
		/** 经纬度（结果） */
		private String location;
		
		/** 偏差距离/米 */
		private Double distance;
		
		/** 国家 */
		private String country;
		
		/** 省份 */
		private String province;
		
		/** 城市 */
		private String city;
		
		/** 城区 */
		private String district;
		
		/** 街道 */
		private String street;
		
		/** AD码 */
		private String adcode;
		
		/** 地区码 */
		private String citycode;
		
		/** 地址简称 */
		private String fmtAddress;
		
		/** 区域描述 */
		private String sematicDesc;
	}
}
