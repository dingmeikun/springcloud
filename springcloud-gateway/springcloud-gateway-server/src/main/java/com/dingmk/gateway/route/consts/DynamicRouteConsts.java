package com.dingmk.gateway.route.consts;

public class DynamicRouteConsts {
	
	/** Redis Hash Key */
	public static final String GATEWAY_ROUTES_HASH_KEY = "GW_ROUTES_HASH_KEY";
	
	/** Local Cache Key */
	public static final String L_GATEWAY_ROUTES_HASH_KEY = "LC_" + GATEWAY_ROUTES_HASH_KEY;
	
	/** Routes create data format */
	public static final String GATEWAY_ROUTES_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
}
