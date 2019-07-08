package com.dingmk.location.gaode.bo;

import lombok.Data;

import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection="test")
public class LocationGaode {

	/** 状态 */
	private int status;
	
	/** 结果集 */
	private Regeocode regeocode;
	
	@Data
	public static class Regeocode {
		private String location;
		private AddressGaode addressComponent;
		private String formatted_address;
	}
	
}
