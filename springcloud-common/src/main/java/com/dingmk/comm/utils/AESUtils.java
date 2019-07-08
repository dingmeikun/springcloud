package com.dingmk.comm.utils;

import com.google.common.base.Preconditions;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Slf4j
public class AESUtils {
    public static final String KEY_ALGORITHM = "AES";
    public static final int KEY_SIZE = 128;
    public static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    public static final byte[] iv1 = new byte[]{(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x90, (byte) 0xAB,
            (byte) 0xCD, (byte) 0xEF, (byte) 0xA9, (byte) 0xB7, (byte) 0xC8, (byte) 0xD6, (byte) 0xE3, (byte) 0xF1,
            (byte) 0x1F, (byte) 0xFE};

    public static byte[] initSecretKey() {
        return initSecretKey(128);
    }

    public static byte[] initSecretKey(int keysize) {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(keysize, new SecureRandom());
            return kg.generateKey().getEncoded();
        } catch (NoSuchAlgorithmException e) {
            log.error("initSecretKey meet NoSuchAlgorithmException");
        }

        return null;
    }

    /**
     * AES128加密(采用默认的"AES/CBC/PKCS5Padding"算法，采用固定的iv1)
     *
     * @param content  明文内容
     * @param password 密钥，密钥长度必须为16字节
     * @return 加密密文
     */
    public static byte[] aes128Encrypt(byte[] content, byte[] password) {
        Preconditions.checkArgument(password.length == 16);
        return aesEncrypt(content, password, ALGORITHM, iv1);
    }

    /**
     * AES128解密(采用默认的"AES/CBC/PKCS5Padding"算法，采用固定的iv1)
     *
     * @param content  密文
     * @param password 密钥，密钥长度必须为16字节
     * @return 解密明文
     */
    public static byte[] aes128Decrypt(byte[] content, byte[] password) {
        Preconditions.checkArgument(password.length == 16);
        return aesDecrypt(content, password, ALGORITHM, iv1);
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
    public static byte[] aesEncrypt(byte[] content, byte[] password, String algorithm, byte[] iv) {
        Preconditions.checkArgument(password.length == 16 || password.length == 32);

        try {
            final SecretKeySpec key = new SecretKeySpec(password, "AES");

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

        log.debug("aesEncrypt key: " + StringUtil.fromBytes(password));

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
    public static byte[] aesDecrypt(byte[] content, byte[] password, String algorithm, byte[] iv) {
        Preconditions.checkArgument(password.length == 16 || password.length == 32);

        try {
            final SecretKeySpec key = new SecretKeySpec(password, "AES");
            final Cipher cipher;
            if (algorithm.endsWith("PKCS7Padding")) {// PKCS7Padding需要使用第三方JCE
                cipher = Cipher.getInstance(algorithm, "BC");
            } else {
                cipher = Cipher.getInstance(algorithm);
            }

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

        log.debug("aesDecrypt key: " + StringUtil.fromBytes(password));

        return null;
    }
}
