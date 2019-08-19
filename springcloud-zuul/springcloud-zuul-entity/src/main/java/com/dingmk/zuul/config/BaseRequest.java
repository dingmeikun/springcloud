package com.dingmk.zuul.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaseRequest {

	/** IP网段 */
    private String ip;

    /** 位置信息 */
    private Location location;

    @Getter
    @Setter
    @ToString
    public static class Location implements Cloneable {
        
        /** 纬度 */
        private String lat;

        /** 经度 */
        private String lng;

        /**
         * 源坐标类型 枚举值：gps;mapbar;baidu;gaode
         */
        private String coordsys;

        @Override
        public Location clone() {
            Location clone = new Location();
            clone.setLat(lat);
            clone.setLng(lng);
            clone.setCoordsys(coordsys);
            return clone;
        }
    }

}
