package com.dingmk.comm.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 采用SHAA加密
 *
 * @author JaviyLee
 */
@Slf4j
public class SHAUtil {
    public static final String SHA1_TYPE = "SHA-1";
    public static final String SHA256_TYPE = "SHA-256";

    /***
     * SHA加密 生成40位SHA码
     *
     * @param
     * @return 返回40位SHA码
     */
    public static String sha1Encode(String inStr) {
        return shaEncode(inStr, SHA1_TYPE);
    }

    /***
     * SHA256加密 生成64位SHA码
     *
     * @param
     * @return 返回64位SHA码
     */
    public static String sha256Encode(String inStr) {
        return shaEncode(inStr, SHA256_TYPE);
    }

    /***
     * SHA加密 生成SHA码
     *
     * @param inStr 待加密字符串
     * @param type    加密类型
     * @return 返回SHA码
     */
    public static String shaEncode(String inStr, String type) {
        try {
            MessageDigest md = MessageDigest.getInstance(type);
            byte[] digest = md.digest(StringUtil.toBytes(inStr));
            return StringUtil.byteToHexStr(digest);
        } catch (NoSuchAlgorithmException e) {
            log.error(String.format("shaEncode meet exception:%s, input:%s", e.getLocalizedMessage(), inStr));
        }

        return "";
    }

    /**
     * SHA加密 生成SHA码
     *
     * @param inStr
     * @param type
     * @param salt
     * @return
     * @throws Exception
     */
    public static String shaEncode(String inStr, String type, String salt) {
        if (StringUtils.hasText(salt)) {
            inStr = inStr + "{" + salt + "}";
        }

        try {
            MessageDigest md = MessageDigest.getInstance(type);
            byte[] digest = md.digest(StringUtil.toBytes(inStr));
            return StringUtil.byteToHexStr(digest);
        } catch (NoSuchAlgorithmException e) {
            log.error(String.format("shaEncode meet exception:%s, input:%s", e.getLocalizedMessage(), inStr));
        }

        return "";
    }
}