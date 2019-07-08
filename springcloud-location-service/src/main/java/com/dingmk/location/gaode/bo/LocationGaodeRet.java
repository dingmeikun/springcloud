package com.dingmk.location.gaode.bo;

import java.util.List;

import lombok.Data;

/**
 * MongoDB高德地理位置查询信息
 * 
 * @author D
 *
 */
@Data
public class LocationGaodeRet {
	
	/** 地理信息集合 */
	private List<Results> results;
	
	/** 查询指数 */
	private Stat stats;
	
	/** 返回参数 1.0 成功， 0.0 失败 */
	private String ok;
	
	@Data
	public static class Stat{
		private String nscanned;
		private String objectsLoaded;
		private String avgDistance;
		private String maxDistance;
		private String time;
	}

	@Data
	public static class Results{
		
		/** MongoDB查询结果偏差距离 */
		private Double dis;
		
		private Result obj;
		
		@Data
		public static class Result {
			
			private String _class;
			private AddressGaode address;
			private List<String> location;
			private String fmtAddress;
		}
	}
}
