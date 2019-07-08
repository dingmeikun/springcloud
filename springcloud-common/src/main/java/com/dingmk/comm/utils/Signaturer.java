/******************************************************************************* 
 * Copyright (C) 2012-2015 Microfountain Technology, Inc. All Rights Reserved. 
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.   
 * Proprietary and confidential
 * 
 * Last Modified: 2015-10-10 11:42:19
 ******************************************************************************/

package com.dingmk.comm.utils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import lombok.extern.slf4j.Slf4j;

/**
 * 签名或验证
 * 
 * @author javiylee
 */
@Slf4j
public class Signaturer {

    public static final String SHA256_WITH_RSA = "SHA256withRSA";

    public static final String MD5_WITH_RSA = "MD5withRSA";

    /**
     * 验证签名
     * 
     * @param pubKeyText
     *            公钥
     * @param plainText
     *            签名的原文
     * @param sign
     *            签名后的内容
     * @return true 验证成功，false 失败。
     */
    public static boolean verifyPower(String pubKeyText, String plainText, String sign) {
        return verify(pubKeyText.getBytes(StandardCharsets.UTF_8), plainText, sign.getBytes());

    }

    /**
     * 验证签名
     * 
     * @param pubKeyText
     *            公钥
     * @param plainText
     *            签名的原文
     * @param sign
     *            签名后的内容
     * @param type
     *            签名方式
     * @return true 验证成功，false 失败。
     */
    public static boolean verifyPower(String pubKeyText, String plainText, String sign, String type) {
        return verify(pubKeyText.getBytes(StandardCharsets.UTF_8), plainText, sign.getBytes(), type);
    }

    /**
     * 验证签名
     * 
     * @param pubKeyText
     *            公钥
     * @param plainText
     *            签名的原文
     * @param sign
     *            签名后的内容
     * @return true 验证成功，false 失败。
     */
    public static boolean verify(String pubKeyText, String plainText, String sign) {
        if (plainText.startsWith("ios_")) {
            return verifyIos(pubKeyText, plainText, sign);
        } else {
            return verify(pubKeyText.getBytes(StandardCharsets.UTF_8), plainText, sign.getBytes());
        }
    }

    public static boolean verifyIos(String pubKeyText, String plainText, String sign) {
        String key = pubKeyText.substring(0, 4);
        key += pubKeyText.substring(pubKeyText.length() - 4);
        log.debug("SIGN_VERIFY_IOS, KEY:{}  PLAINTEXT:{}  SIGN:{}", key, plainText, sign);
        try {
            String value = DES.encryptDES(plainText, key);
            if (value.equals(sign)) {
                log.debug("SIGN_CHECK_SUCCESS, PLAINTEXT:{}", plainText);
                return true;
            } else {
                log.warn("SIGN_CHECK_FAIL, PLAINTEXT:{}", plainText);
            }

        } catch (Exception e) {
            log.warn("SIGN_CHECK_ERROR, PLAINTEXT:{}  ERROR:{}", plainText, e.getMessage(), e);
        }
        return false;
    }

    public static String sign(String priKeyText, String plainText) {
        return sign(priKeyText, plainText, MD5_WITH_RSA);
    }

    /**
     * 用私钥签名
     * 
     * @param priKeyText
     *            私钥
     * @param plainText
     *            签名的原文
     * @param type
     *            签名方式
     * @return 签名结果
     */
    public static String sign(String priKeyText, String plainText, String type) {
        return new String(sign(priKeyText.getBytes(StandardCharsets.UTF_8), plainText, type));
    }

    public static byte[] sign(byte[] priKeyText, String plainText, String type) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(priKeyText));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey prikey = keyf.generatePrivate(priPKCS8);

            // 用私钥对信息生成数字签名
            java.security.Signature signet = java.security.Signature.getInstance(type);
            signet.initSign(prikey);
            signet.update(plainText.getBytes(StandardCharsets.UTF_8));
            byte[] signed = Base64.encodeToByte(signet.sign());
            return signed;
        } catch (java.lang.Exception e) {
            log.warn("SIGN_CHECK_ERROR, PLAINTEXT:{}  ERROR:{}", plainText, e.getMessage(), e);
        }
        return null;
    }

    /**
     * Description:校验数字签名,此方法不会抛出任务异�?成功返回true,失败返回false,要求全部参数不能为空
     * 
     * @param pubKeyText
     *            公钥,base64编码
     * @param plainText
     *            明文
     * @param signText
     *            数字签名的密文base64编码
     * @return 校验成功返回true 失败返回false
     */
    public static boolean verify(byte[] pubKeyText, String plainText, byte[] signText) {
        return verify(pubKeyText, plainText, signText, MD5_WITH_RSA);
    }

    /**
     * 校验数字签名
     * 
     * @desc 全部参数不能为空；此方法不会抛出任务异常
     * @param pubKeyText
     *            公钥,base64编码
     * @param plainText
     *            明文
     * @param signText
     *            数字签名的密文base64编码
     * @param type
     *            签名类型
     * @return 校验成功返回true；失败返回false
     */
    public static boolean verify(byte[] pubKeyText, String plainText, byte[] signText, String type) {
        try {
            Signature signatureChecker = getSignature(pubKeyText, plainText, type);
            // 验证签名是否正常
            // 解密由base64编码的数字签名
            byte[] signed = Base64.decode(signText);
            return signatureChecker.verify(signed);
        } catch (Throwable e) {
            log.warn("SIGN_CHECK_ERROR, PLAINTEXT:{}  ERROR:{}", plainText, e.getMessage(), e);
            return false;
        }
    }

    private static Signature getSignature(byte[] pubKeyText, String plainText, String type)
            throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        // 解密由base64编码的公钥；并构造X509EncodedKeySpec对象
        java.security.spec.X509EncodedKeySpec bobPubKeySpec = new java.security.spec.X509EncodedKeySpec(
                Base64.decode(pubKeyText));
        // RSA对称加密算法
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
        Signature checker = Signature.getInstance(type);
        checker.initVerify(pubKey);
        checker.update(plainText.getBytes(StandardCharsets.UTF_8));
        return checker;
    }
}
