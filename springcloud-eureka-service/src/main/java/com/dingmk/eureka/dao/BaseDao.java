package com.dingmk.eureka.dao;

import org.springframework.stereotype.Repository;

import com.dingmk.eureka.model.BaseRequest.Location;
import com.dingmk.eureka.model.BaseResponse.Address;

@Repository
public class BaseDao {

	public Address getAddress(Location location) {
		Address address = new Address();
		
		// TODO use mongo/mysql template client get data
		address.setLocation(location.getLng() + "" + location.getLat());
		address.setCountry("中国");
		address.setCity("深圳");
		
		return address;
	}
}
