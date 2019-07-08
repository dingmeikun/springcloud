package com.dingmk.location.util;

/**
 * 合作企业名称
 * 
 * @author D
 *
 */
public enum LocationType {

	GAODE("gaode", 1),
	
	BAIDU("baidu", 2),
	
	GPS("gps", 3),
	
	MAPBAR("mapbar", 4);
	
	private String name;
	private int idx;
	
	LocationType(String name){
		this.name = name;
	}
	
	LocationType(String name, int idx){
		this.name = name;
		this.idx = idx;
	}
	
	public String getName() {
		return name;
	}
	
	public int getIdx() {
		return idx;
	}
}
