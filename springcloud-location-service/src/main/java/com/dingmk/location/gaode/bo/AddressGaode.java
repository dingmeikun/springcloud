package com.dingmk.location.gaode.bo;

import java.util.List;

import lombok.Data;

@Data
public class AddressGaode {
	
	private String city;
	private String province;
	private String adcode;
	private String district;
	private String towncode;
	private Building building;
	private Street streetNumber;
	private String country;
	private String township;
	private String seaArea;
	private Neighbor neighborhood;
	private String citycode;
	private List<Business> businessAreas;
}