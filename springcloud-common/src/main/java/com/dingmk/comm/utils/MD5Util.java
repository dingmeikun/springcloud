/*
 * PROJECT NAME: openplatform
 * CREATED TIME: 15-4-30 下午4:16
 *       AUTHOR: lizhiming
 *    COPYRIGHT: Copyright(c) 2015~2020 All Rights Reserved.
 *
 */
package com.dingmk.comm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.apache.commons.io.IOUtils;

import com.dingmk.comm.constvar.SysConstVar;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lizhiming
 */
@Slf4j
public class MD5Util {
    /**
     * MD5值有效长度
     */
    public static final int MD5_LENGTH = 32;

    /**
     * MD5计算
     *
     * @param str
     * @return
     */
    public static String md5Crypt(String str) {
        try {
            byte[] inputBytes = str.getBytes(SysConstVar.DEFAULT_CHARSET);
            return md5Crypt(inputBytes);
        } catch (UnsupportedEncodingException e) {
            log.error(String.format("md5Crypt meet exception:%s, input:%s", e.getLocalizedMessage(), str));
        }

        return "";
    }

    /**
     * MD5散列
     *
     * @param keyBytes
     * @return
     */
    public static String md5Crypt(byte[] keyBytes) {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");

            // 使用指定的字节更新摘要
            mdInst.update(keyBytes);

            // 获得密文
            byte[] md = mdInst.digest();

            // 把密文转换成十六进制的字符串形式
            return StringUtil.byteToHexStr(md);
        } catch (Exception e) {
            log.error("md5Crypt meet exception:{}, input:{}", e.getLocalizedMessage(), StringUtil.fromBytes(keyBytes));
        }

        return "";
    }

    /**
     * 对文件进行MD5计算
     *
     * @param file
     * @return
     */
    public static String md5Crypt(File file) {
        FileInputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);

            MessageDigest mdInst = MessageDigest.getInstance("MD5");

            byte[] readBuff = new byte[SysConstVar.STREAM_BUFF_SIZE];

            int length = -1;
            while ((length = inputStream.read(readBuff, 0, SysConstVar.STREAM_BUFF_SIZE)) != -1) {
                mdInst.update(readBuff, 0, length);
            }

            // 获得密文
            byte[] md = mdInst.digest();

            // 把密文转换成十六进制的字符串形式
            return StringUtil.byteToHexStr(md);
        } catch (Exception e) {
            log.error(String.format("md5crypt meet exception:%s, filePath:%s", e.getLocalizedMessage(), file.getPath()));
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        return "";
    }

    /**
     * MD5散列
     *
     * @param keyBytes
     * @param salt
     * @return
     */
    public static String md5Crypt(byte[] keyBytes, String salt) {
        String strKey = new String(keyBytes, StandardCharsets.UTF_8);

        if (salt != null && "".equals(salt) == false) {
            strKey = strKey + "{" + salt.toString() + "}";
        }

        return md5Crypt(strKey.getBytes(StandardCharsets.UTF_8));
    }
}
