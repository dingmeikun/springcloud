package com.dingmk.location.gaode.bo;

import lombok.Data;

@Data
public class LocationGaodeRes {

	/** 详细地址 */
	private AddressGaode address;
	
	/** 精简位置  */
	private String fmtAddress;
}
