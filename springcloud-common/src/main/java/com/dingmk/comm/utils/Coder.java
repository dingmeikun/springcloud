/******************************************************************************* 
 * Copyright (C) 2012-2015 Microfountain Technology, Inc. All Rights Reserved. 
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.   
 * Proprietary and confidential
 *
 * Last Modified: 2016-12-13 12:18:04
 ******************************************************************************/

package com.dingmk.comm.utils;

import java.security.MessageDigest;
import java.util.Random;

/**
 * @author javiylee
 */
public class Coder {

    public static final String KEY_SHA = "SHA";

    public static final String KEY_SHA_256 = "SHA-256";

    public static final String KEY_MD5 = "MD5";

    static final String[] seeds = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "!", "#", "$", "%", "^", "*"};

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};

    public static String random(int len, int s, int e) {
        if (len < 1) {
            len = 8;
        }

        Random random = new Random();
        int seedsLen = seeds.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int idx = random.nextInt(seedsLen);
            sb.append(seeds[idx]);
        }
        return sb.toString();
    }

    /**
     * MD5加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptMD5(byte[] data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data);
        return md5.digest();
    }

    /**
     * SHA加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptSHA(byte[] data) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);
        return sha.digest();
    }

    /**
     * SHA-256加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptSHA256(byte[] data) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA_256);
        sha.update(data);
        return sha.digest();
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || "".equals(hexString)) {
            return null;
        }
        hexString = hexString.toLowerCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789abcdef".indexOf(c);
    }

    public static String toHexString(byte[] b) {

        StringBuilder sb = new StringBuilder(b.length * 2);

        for (int i = 0; i < b.length; i++) {

            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);

            sb.append(HEX_DIGITS[b[i] & 0x0f]);

        }

        return sb.toString();

    }

    /**
     * 将字符串md5 字节数组，再转成16进制字符串
     *
     * @param src
     * @return
     */
    public static String md5ToHexString(String src) {
        return md5ToHexString(src, "UTF-8");
    }

    public static String md5ToHexString(String src, String encoding) {
        try {
            return toHexString(encryptMD5(src.getBytes(encoding)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将字符串SHA 字节数组，再转成16进制字符串
     *
     * @param src
     * @return
     */
    public static String shaToHexString(String src) {
        return shaToHexString(src, "UTF-8");
    }

    public static String shaToHexString(String src, String encoding) {
        try {
            return toHexString(encryptSHA(src.getBytes(encoding)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将字符串SHA256 字节数组，再转成16进制字符串
     *
     * @param src
     * @return
     */
    public static String sha256ToHexString(String src) {
        return sha256ToHexString(src, "UTF-8");
    }

    public static String sha256ToHexString(String src, String encoding) {
        try {
            return toHexString(encryptSHA256(src.getBytes(encoding)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
