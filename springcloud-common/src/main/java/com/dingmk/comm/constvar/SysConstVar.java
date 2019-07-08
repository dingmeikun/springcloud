/******************************************************************************* 
 * Copyright (C) 2012-2015 Microfountain Technology, Inc. All Rights Reserved. 
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.   
 * Proprietary and confidential
 *
 * Last Modified: 2016-5-11 11:02:58
 ******************************************************************************/

package com.dingmk.comm.constvar;

import java.io.File;
import java.math.BigDecimal;

/**
 * @author lizhiming
 */
public class SysConstVar {

    /**
     * 默认字符集
     */
    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 无效ID
     */
    public static final long INVALID_ID = -1;

    public static final BigDecimal DECIMAL_ZERO = new BigDecimal(0.00);

    public static final BigDecimal DECIMAL_PRICE_NEED_DISCUSS = new BigDecimal(-1.00);

    /**
     * 文件操作时流缓存的大小，单位字节
     */
    public static final int STREAM_BUFF_SIZE = 64 * 1024;

    /**
     * JDBC配置加密密钥
     */
    public static final String JDBC_CONFIG_PASSWORD = "xykj*jdbc#201604";

    public static final String PUBLIC_AES_KEY = "xykj#201612*key?";
    public static final String PUBLIC_FIX_SALT = "6f5de3067e38e33e";

    /**
     * 时间格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String YYYY_MM_DD_DATE_FORMAT = "yyyy-MM-dd";

    public static final String YYYYMMDD_DATA_FORMAT = "yyyyMMdd";

    public static final String HHMMSS_DATA_FORMAT = "HHmmss";

    public static final String YYYYMMDDHHMMSSSSS_DATA_FORMAT = "yyyyMMddHHmmssSSS";

    public static final String YYYYMMDDHHMMSS_DATA_FORMAT = "yyyyMMddHHmmss";

    public static final String YYMMDDHHMMSS_DATA_FORMAT = "yyMMddHHmmss";

    public static final String YYMMDDHHMM_DATA_FORMAT = "yyyyMMddHHmm";


    /**
     * 值分隔符，纪录分隔符
     */
    public static final String VALUE_SEPERATOR = "#";

    public static final String RECORD_SEPERATOR = "; ";

    public static final String NEWLINE_SEPERATOR = System.getProperty("line.separator");

    public static final String PATH_SEPERATOR = File.separator;

    /**
     * 优先级
     */
    public static final short PRIORITY_MAX = 9;

    public static final short PRIORITY_MIN = 0;

    public static final short PRIORITY_DEFAULT = PRIORITY_MIN;

    /**
     * 打印消息起、止分隔标志
     */
    public static final String DUMP_MSG_BEGIN_SEP = "DUMP_MSG_BEGIN>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";

    public static final String DUMP_MSG_END_SEP = "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<DUMP_MSG_END\n";

    /**
     * 默认分页参数
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 毫秒数
     */
    public static final long TIMEMILLIS_SECOND = 1000;

    public static final long TIMEMILLIS_MINUTE = 60 * TIMEMILLIS_SECOND;

    public static final long TIMEMILLIS_HOUR = 60 * TIMEMILLIS_MINUTE;

    public static final long TIMEMILLIS_DAY = 24 * TIMEMILLIS_HOUR;
}
