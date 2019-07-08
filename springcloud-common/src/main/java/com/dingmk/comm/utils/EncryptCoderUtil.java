/*
 * PROJECT NAME: openplatform
 * CREATED TIME: 15-4-30 下午4:16
 *       AUTHOR: lizhiming
 *    COPYRIGHT: Copyright(c) 2015~2020 All Rights Reserved.
 *
 */

package com.dingmk.comm.utils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.dingmk.comm.constvar.SysConstVar;
import com.google.common.base.Preconditions;

import lombok.extern.slf4j.Slf4j;

/**
 * 加密、解密工具类
 *
 * @author lizhiming
 */
@Slf4j
public final class EncryptCoderUtil {
    // 默认私钥, 使用的时候一般后面可再加上一个时间戳（14字符）
    public static final String DEFAULT_PRIVATE_KEY = "cf0f134aad743c1f";

    /**
     * 工具类不允许实例化
     */
    private EncryptCoderUtil() {
    }

    /**
     * AES128加密要求密钥长度为128，即16字节，有时候并不好确保密钥的长度刚好
     * 故可以以参数为输入，进行MD5，然后取MD5结果的前16字节当密钥
     *
     * @param input
     * @return
     */
    public static String gen128PrivateKey(String input) {
        Preconditions.checkNotNull(input);

        String md5Result = MD5Util.md5Crypt(input);
        return md5Result.substring(0, 16);
    }

    /**
     * AES128加密要求密钥长度为128，即16字节，有时候并不好确保密钥的长度刚好
     * AES256加密要求密钥长度为256，即32字节，有时候并不好确保密钥的长度刚好
     * 故可以以参数为输入，进行MD5，然后取MD5结果的前16字节 or 32字节当密钥
     *
     * @param input  原始密钥
     * @param keyLen 密钥长度（字节数），必须为16 or 32
     * @return
     */
    public static String genAESPrivateKey(String input, int keyLen) {
        Preconditions.checkNotNull(input);
        Preconditions.checkArgument(keyLen == 16 || keyLen == 32);

        String md5Result = MD5Util.md5Crypt(input);
        return md5Result.substring(0, keyLen);
    }

    /**
     * @param rawKeyData 密钥
     * @param rawData    明文
     * @return 密文
     * @deprecated DES加密
     */
    @Deprecated
    public static String desEncrypt(String rawKeyData, String rawData) {
        String result = "";

        try {
            final SecureRandom sr = new SecureRandom();

            final DESKeySpec dks = new DESKeySpec(rawKeyData.getBytes(StandardCharsets.UTF_8));

            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            final SecretKey key = keyFactory.generateSecret(dks);

            final Cipher cipher = Cipher.getInstance("DES");

            cipher.init(Cipher.ENCRYPT_MODE, key, sr);

            final byte[] data = rawData.getBytes();

            final byte[] encryptedData = cipher.doFinal(data);

            result = new String(encryptedData, StandardCharsets.UTF_8);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * @param rawKeyData    密钥
     * @param encryptedData 密文
     * @return 明文
     * @deprecated DES解密
     */
    @Deprecated
    public static String desDecrypt(String rawKeyData, String encryptedData) {
        String result = "";

        try {
            final SecureRandom sr = new SecureRandom();
            final DESKeySpec dks = new DESKeySpec(rawKeyData.getBytes(StandardCharsets.UTF_8));
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            final SecretKey key = keyFactory.generateSecret(dks);
            final Cipher cipher = Cipher.getInstance("DES");

            cipher.init(Cipher.DECRYPT_MODE, key, sr);

            final byte[] decryptedData = cipher.doFinal(encryptedData.getBytes());

            result = new String(decryptedData, StandardCharsets.UTF_8);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return result;
    }

//    private static byte[] getRawAES128Key(byte[] seed) throws Exception {
//        return seed;
//
//        /* 以下方法在一台机器上加密，解密测试OK；但一台机器加密，另一台机器解密，则同一输入值在加密和解密的返回值不一样
//        KeyGenerator kgen = KeyGenerator.getInstance("AES");
//        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
//        sr.setSeed(seed);
//        kgen.init(128, sr); // 192 and 256 bits may not be available
//
//        SecretKey skey = kgen.generateKey();
//
//        return skey.getEncoded();
//        */
//    }

    /**
     * AES128加密
     *
     * @param content  明文内容
     * @param password 密钥，密钥长度必须为16字节
     * @return 加密密文
     */
    @Deprecated
    public static byte[] aes128Encrypt(byte[] content, String password) {
        Preconditions.checkArgument(password.length() == 16);
        return aesEncrypt(content, password, "AES/ECB/PKCS5Padding", null);
    }

    /**
     * AES128解密
     *
     * @param content  密文
     * @param password 密钥，密钥长度必须为16字节
     * @return 解密明文
     */
    @Deprecated
    public static byte[] aes128Decrypt(byte[] content, String password) {
        Preconditions.checkArgument(password.length() == 16);
        return aesDecrypt(content, password, "AES/ECB/PKCS5Padding", null);
    }

    /**
     * AES加密
     * 使用AES加密时，当密钥大于128时，代码会抛出java.security.InvalidKeyException: Illegal key size or default parameters
     * 参考 http://blog.csdn.net/shangpusp/article/details/7416603
     *
     * @param content   明文内容
     * @param password  密钥，密钥长度必须为16 or 32字节
     * @param algorithm AES/ECB/PKCS5Padding, AES/CBC/PKCS5Padding
     * @param iv
     * @return 加密密文
     */
    public static byte[] aesEncrypt(byte[] content, String password, String algorithm, byte[] iv) {
        Preconditions.checkArgument(password.length() == 16 || password.length() == 32);

        try {
            final SecretKeySpec key = new SecretKeySpec(password.getBytes(SysConstVar.DEFAULT_CHARSET), "AES");

            final Cipher cipher = Cipher.getInstance(algorithm);

            if (iv != null && iv.length > 0) {
                cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            }

            return cipher.doFinal(content);
        } catch (NoSuchAlgorithmException e) {
            log.error("AESEncrypt meet exception. " + e.getLocalizedMessage());
        } catch (NoSuchPaddingException e) {
            log.error("AESEncrypt meet exception. " + e.getLocalizedMessage());
        } catch (InvalidKeyException e) {
            log.error("AESEncrypt meet exception. " + e.getLocalizedMessage());
        } catch (IllegalBlockSizeException e) {
            log.error("AESEncrypt meet exception. " + e.getLocalizedMessage());
        } catch (BadPaddingException e) {
            log.error("AESEncrypt meet exception. " + e.getLocalizedMessage());
        } catch (Exception e) {
            log.error("AESEncrypt meet exception. " + e.getLocalizedMessage());
        }

        log.debug("aesEncrypt key: " + password);

        return null;
    }

    /**
     * AES解密
     *
     * @param content   密文
     * @param password  密钥，密钥长度必须为16 or 32字节
     * @param algorithm AES/ECB/PKCS5Padding, AES/CBC/PKCS5Padding
     * @param iv
     * @return 解密明文
     */
    public static byte[] aesDecrypt(byte[] content, String password, String algorithm, byte[] iv) {
        Preconditions.checkArgument(password.length() == 16 || password.length() == 32);

        try {
            final SecretKeySpec key = new SecretKeySpec(password.getBytes(SysConstVar.DEFAULT_CHARSET), "AES");

            final Cipher cipher = Cipher.getInstance(algorithm);
            if (iv != null && iv.length > 0) {
                cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            } else {
                cipher.init(Cipher.DECRYPT_MODE, key);
            }

            return cipher.doFinal(content);
        } catch (NoSuchAlgorithmException e) {
            log.error("AESDecrypt meet exception. " + e.getLocalizedMessage());
        } catch (NoSuchPaddingException e) {
            log.error("AESDecrypt meet exception. " + e.getLocalizedMessage());
        } catch (InvalidKeyException e) {
            log.error("AESDecrypt meet exception. " + e.getLocalizedMessage());
        } catch (IllegalBlockSizeException e) {
            log.error("AESDecrypt meet exception. " + e.getLocalizedMessage());
        } catch (BadPaddingException e) {
            log.error("AESDecrypt meet exception. " + e.getLocalizedMessage());
        } catch (Exception e) {
            log.error("AESDecrypt meet exception. " + e.getLocalizedMessage());
        }

        log.debug("aesDecrypt key: " + password);

        return null;
    }

//    public static void main(String[] argv) {
//        String strSQLURL = "jdbc:mysql://192.168.1.96:3306/sms_collect_only?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true";
//        String strSQLUsername = "root";
//        String strSQLPasswd = "xy123456";
//
//        byte[] encryptSQLURL = EncryptCoderUtil.aes128Encrypt(strSQLURL.getBytes(), SysConstVar.JDBC_CONFIG_PASSWORD);
//        byte[] encryptSQLUsername = EncryptCoderUtil.aes128Encrypt(strSQLUsername.getBytes(), SysConstVar.JDBC_CONFIG_PASSWORD);
//        byte[] encryptSQLPasswd = EncryptCoderUtil.aes128Encrypt(strSQLPasswd.getBytes(), SysConstVar.JDBC_CONFIG_PASSWORD);
//
//        byte[] base64SQLURL = StringUtil.base64Encode(encryptSQLURL);
//        byte[] base64SQLUsername = StringUtil.base64Encode(encryptSQLUsername);
//        byte[] base64SQLPasswd = StringUtil.base64Encode(encryptSQLPasswd);
//
//        String resultSQLURL = new String(base64SQLURL);
//        String resultSQLUsername = new String(base64SQLUsername);
//        String resultSQLPasswd = new String(base64SQLPasswd);
//
//        log.debug("encrypt url result :{}", resultSQLURL);
//        log.debug("encrypt username result :{}", resultSQLUsername);
//        log.debug("encrypt passwd result :{}", resultSQLPasswd);
//    }
}
