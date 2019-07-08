/*
 * PROJECT NAME: openplatform
 * CREATED TIME: 15-4-30 下午4:16
 *       AUTHOR: lizhiming
 *    COPYRIGHT: Copyright(c) 2015~2020 All Rights Reserved.
 *
 */
package com.dingmk.comm.constvar;

/**
 * @author lizhiming
 */
public class TypeConstVar {
    public static final short INVALID_TYPE = -1;

    public static final short CARRIER_TYPE_UNKNOWN = 0;     // 未知运营商
    public static final short CARRIER_TYPE_YIDONG = 1;      // 移动:1
    public static final short CARRIER_TYPE_LIANTONG = 3;    // 联通:3
    public static final short CARRIER_TYPE_DIANXIN = 6;     // 电信:6

    /**
     * 客户端类型，用来记录用户通过什么途径访问
     */
    public static final short Client_Type_UNKNOWN = -1;     // 未知
    public static final short Client_Type_WEB = 0;          // Web访问
    public static final short Client_Type_WeiXin = 1;       // 微信访问
    public static final short Client_Type_Android = 2;      // Android访问
    public static final short Client_Type_IOS = 3;          // IOS访问
    public static final short Client_Type_SDK = 4;          // XY SDK客户端
    public static final short Client_Type_API = 5;          // API访问
}
