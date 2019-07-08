package com.dingmk.comm.utils;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class DES {

    private static byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};

    //private static byte[] iv = {0, 0, 0,0, 0, 0, 0, 0 };
    public static String getRamod() {
        java.util.Random rd = new java.util.Random();
        int v = 99999999;
        int s = rd.nextInt(v);
        String temp = s + "";
//         if(temp != null){
//             int len = 8-temp.length();
//             for(int i = 0 ; i < len; i++){
//                 temp +="0";
//             }
//         }
        return temp;
    }

    public static String encryptDES(String encryptString, String encryptKey)
            throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        //IvParameterSpec zeroIv =null;
        SecretKeySpec key = new SecretKeySpec(StringUtil.toBytes(encryptKey), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        byte[] encryptedData = cipher.doFinal(StringUtil.toBytes(encryptString));

        return Base64.encode(encryptedData);
    }

    public static String decryptDES(String decryptString, String decryptKey)
            throws Exception {
        byte[] byteMi = Base64.decode(decryptString);
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        //IvParameterSpec zeroIv =null;
        SecretKeySpec key = new SecretKeySpec(StringUtil.toBytes(decryptKey), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        byte[] decryptedData = cipher.doFinal(byteMi);

        return new String(decryptedData, "UTF-8");
    }

    /**
     * DES加密字符串
     */
    public static byte[] desString(String plain, String key) {
        byte[] encryptedData = null;
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();

            // 从原始密钥数据创建DESKeySpec对象, DES 要求秘钥是 64bit的
            DESKeySpec dks = new DESKeySpec(StringUtil.toBytes(key));

            // 创建一个密匙工厂，然后用它把DESKeySpec转换成 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);

            // using DES in ECB mode
            Cipher cipher = Cipher.getInstance("DES");

            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);

            // 执行加密操作
            encryptedData = cipher.doFinal(StringUtil.toBytes(plain));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptedData;
    }

    public static byte[] des_decrypt(byte[] encrypted, byte[] key) {
        if (key == null || encrypted == null) {
            return null;
        }

        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();

            // 从原始密钥数据创建DESKeySpec对象
            // 由于 DES 要求秘钥是 64bit 的，而用户直接输入的 key 可能长度不够，这里简单点，先对 key 进行 md5，截断取前
            // 8 个字节
            DESKeySpec dks = new DESKeySpec(key);

            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);

            // using DES in ECB mode
            Cipher cipher = Cipher.getInstance("DES");

            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);

            // 正式执行解密操作
            byte[] decryptedData = cipher.doFinal(encrypted);

            return decryptedData;

        } catch (Throwable th) {

        }

        return null;
    }
}
