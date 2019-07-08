package com.dingmk.comm.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;

/**
 * 根据IP地址获取所在城市
 * 参考http://ip.taobao.com/index.php
 *
 * @author lizhiming on 2015/7/22.
 */
@Slf4j
public final class IPAddressUtil {

    /**
     * 获取本机IP地址
     *
     * @return
     */
    public static String getLocalAddressIP() {
        InetAddress local = null;

        try {
            local = InetAddress.getLocalHost();
        } catch (Exception e) {
            // 忽略即可
        }

        return (local == null) ? "127.0.0.1" : local.getHostAddress();
    }

    public static long ip2Long(String ipAddress) {
        long result = 0;

        String[] ipAddressInArray = ipAddress.split("\\.");

        for (int i = 3; i >= 0; i--) {
            long ip = Long.parseLong(ipAddressInArray[3 - i]);
            if (ip < 0 || ip > 255) {
                log.error("ip2Long only support ipv4, please check the ip:{}", ipAddress);
                return -1;
            }
            // left shifting 24,16,8,0 and bitwise OR
            // 1. 127 << 24
            // 1. 0 << 16
            // 1. 0 << 8
            // 1. 1 << 0
            result |= ip << (i * 8);
        }

        return result;
    }

    public static String long2IP(long ip) {
        return ((ip >> 24) & 0xFF) +
                "." + ((ip >> 16) & 0xFF) +
                "." + ((ip >> 8) & 0xFF) +
                "." + (ip & 0xFF);
    }
}

