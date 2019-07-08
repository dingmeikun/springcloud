package com.dingmk.location.composit;

import com.dingmk.location.baidu.bo.LocationBaidu;
import com.dingmk.location.gaode.bo.LocationGaode;

import lombok.Data;

/**
 * 复合第三方API查询数据对象
 * 
 * @author Dingmk
 *
 */
@Data
public class CompositAPILocData {

	private String coordsys;
	private LocationBaidu locationBaidu;
	private LocationGaode locationGaode; 
}
