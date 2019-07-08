/******************************************************************************* 
 * Copyright (C) 2012-2015 Microfountain Technology, Inc. All Rights Reserved. 
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.   
 * Proprietary and confidential
 * 
 * Last Modified: 2016-10-11 11:00:21
 ******************************************************************************/

package com.dingmk.comm.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.SecureRandom;

public class DESUtils {

    public static final byte[] DEFAULT_IV = { (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x90,
            (byte) 0xAB, (byte) 0xCD, (byte) 0xEF };

    public static final IvParameterSpec DEFAULT_IVPS = new IvParameterSpec(DEFAULT_IV);

    /**
     * DES加密字符串
     * 
     * @param plain
     *            要加密的字符串
     * @param seed
     *            密钥种子
     */
    public static byte[] encry(String plain, String seed) {
        try {
            return encry(plain.getBytes("UTF-8"), seed.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encry(byte[] data, byte[] seed) {
        return encry(data, seed, false);
    }

    public static byte[] encry(byte[] data, byte[] seed, boolean padding) {
        return encry(data, seed, padding, DEFAULT_IV);
    }

    public static byte[] encry(byte[] data, byte[] seed, boolean padding, byte[] ivbts) {
        byte[] encryptedData = null;
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();

            // 从原始密钥数据创建DESKeySpec对象, DES 要求秘钥是 64bit的
            DESKeySpec dks = new DESKeySpec(seed);

            String type = null;
            if (padding) {
                type = "DES/CBC/PKCS5Padding";
            } else {
                type = "DES";
            }

            // 创建一个密匙工厂，然后用它把DESKeySpec转换成 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);

            // using DES in ECB mode
            Cipher cipher = Cipher.getInstance(type);

            // 用密匙初始化Cipher对象
            if (padding) {
                IvParameterSpec iv = new IvParameterSpec(ivbts);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);
            }

            // 执行加密操作
            encryptedData = cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptedData;
    }

    /** DES解密 
     * @throws InvalidKeyException */
    public static byte[] decryptData(byte[] encrypted, byte[] seed) throws Exception {
        return decryptData(encrypted, seed, false);
    }

    /** DES解密 
     * @throws InvalidKeyException */
    public static byte[] decryptData(byte[] encrypted, byte[] seed, boolean padding) throws Exception {
        return decryptData(encrypted, seed, padding, DEFAULT_IV);
    }

    /** DES解密 
     * @throws InvalidKeyException */
    public static byte[] decryptData(byte[] encrypted, byte[] seed, boolean padding, byte[] ivbts) throws Exception {
        if (seed == null || encrypted == null) {
            return null;
        }

        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        // 由于 DES 要求秘钥是 64bit 的，而用户直接输入的 key 可能长度不够，这里简单点，先对 key 进行 md5，截断取前
        // 8 个字节
        DESKeySpec dks = new DESKeySpec(seed);

        String type = null;
        if (padding) {
            type = "DES/CBC/PKCS5Padding";
        } else {
            type = "DES";
        }

        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
        // 一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(dks);

        // using DES in ECB mode
        Cipher cipher = Cipher.getInstance(type);

        // 用密匙初始化Cipher对象
        // 用密匙初始化Cipher对象
        if (padding) {
            IvParameterSpec iv = new IvParameterSpec(ivbts);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);
        }

        // 正式执行解密操作
        byte[] decryptedData = cipher.doFinal(encrypted);

        return decryptedData;
    }

//    public static void main(String[] args) {
//        String s = "abc";
//        byte[] bs = encry(s.getBytes(), "abcdefgh".getBytes(), true);
//        bs = decryptData(bs, "abcdefgh".getBytes(), true);
//        System.out.println(new String(bs));
//    }

}
