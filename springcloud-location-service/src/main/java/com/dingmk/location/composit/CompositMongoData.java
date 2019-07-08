package com.dingmk.location.composit;

import com.dingmk.location.baidu.bo.LocationBaiduPo;
import com.dingmk.location.gaode.bo.LocationGaodePo;

import lombok.Data;

@Data
public class CompositMongoData {

	private String coordsys;
	private LocationBaiduPo locationBaiduPo;
	private LocationGaodePo locationGaodePo;
}
