package com.dingmk.comm.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/** */

/**
 * <p>
 * RSA公钥/私钥/签名工具包
 * </p>
 * <p>
 * 罗纳德·李维斯特（Ron [R]ivest）、阿迪·萨莫尔（Adi [S]hamir）和伦纳德·阿德曼（Leonard [A]dleman）
 * </p>
 * <p>
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br/>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br/>
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 * </p>
 *
 * @author IceWee
 * @version 1.0
 * @date 2012-4-26
 */
public class RSAUtils {
    
    /** 加密算法RSA */
    public static final String KEY_ALGORITHM = "RSA";

    /** 签名算法 */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /** 获取公钥的key */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /** 获取私钥的key */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /** RSA最大加密明文大小 */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /** RSA最大解密密文大小 */
    //private static final int MAX_DECRYPT_BLOCK = 128;

    /** RSA加解密逻辑 */
    private static final String ALGORITHM = "RSA/ECB/PKCS1Padding";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair1024() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    public static Map<String, Object> genKeyPair2048() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(2048);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * <p>
     * 获取私钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return StringUtil.fromBytes(Base64.encode(key.getEncoded()));
    }

    /**
     * <p>
     * 获取公钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return StringUtil.fromBytes(Base64.encode(key.getEncoded()));
    }

    /**
     * <p>
     * 用私钥对信息生成数字签名
     * </p>
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    @Deprecated
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return StringUtil.fromBytes(Base64.encode(signature.sign()));
    }

    /**
     * <p>
     * 校验数字签名
     * </p>
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    @Deprecated
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.decode(sign));
    }

    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {
        return decryptByPrivateKey(encryptedData, privateKey, ALGORITHM, 2048);
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
            throws Exception {
        return decryptByPublicKey(encryptedData, publicKey, ALGORITHM, 2048);
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        return encryptByPublicKey(data, publicKey, ALGORITHM);
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        return encryptByPrivateKey(data, privateKey, ALGORITHM);
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey, String algorithm)
            throws Exception {
        byte[] encryptedData;
        if (null != algorithm) {
            byte[] keyBytes = Base64.decode(publicKey);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            // 对数据加密
            Cipher cipher;
            if (algorithm.endsWith("OAEPPadding")) {//第三方JCE
                cipher = Cipher.getInstance(algorithm, "BC");
            } else {
                cipher = Cipher.getInstance(algorithm);
            }
            cipher.init(Cipher.ENCRYPT_MODE, publicK);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            encryptedData = out.toByteArray();
            out.close();
        } else {
            throw new NullPointerException("加密的algorithm 为空");
        }

        return encryptedData;
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey, String algorithm)
            throws Exception {
        byte[] encryptedData;
        if (null != algorithm) {
            byte[] keyBytes = Base64.decode(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            // 对数据加密
            Cipher cipher;
            if (algorithm.endsWith("OAEPPadding")) {//第三方JCE
                cipher = Cipher.getInstance(algorithm, "BC");
            } else {
                cipher = Cipher.getInstance(algorithm);
            }
            cipher.init(Cipher.ENCRYPT_MODE, privateK);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            encryptedData = out.toByteArray();
            out.close();
        } else {
            throw new NullPointerException("加密的algorithm 为空");
        }

        return encryptedData;
    }

    /**
     * @param encryptedData 已加密的数据
     * @param privateKey    私钥(BASE64编码)
     * @param algorithm     算法名称
     * @param keyLength     密钥长度
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey,
                                             String algorithm, int keyLength) throws Exception {
        byte[] decryptedData;
        if (null != algorithm) {
            byte[] keyBytes = Base64.decode(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher;
            if (algorithm.endsWith("OAEPPadding")) {//第三方JCE
                cipher = Cipher.getInstance(algorithm, "BC");
            } else {
                cipher = Cipher.getInstance(algorithm);
            }
            cipher.init(Cipher.DECRYPT_MODE, privateK);
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            int decryptBlock = keyLength / 8;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > decryptBlock) {
                    cache = cipher.doFinal(encryptedData, offSet, decryptBlock);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * decryptBlock;
            }
            decryptedData = out.toByteArray();
            out.close();
        } else {
            throw new NullPointerException("解密的algorithm为空");
        }

        return decryptedData;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDr7snzhMyjBQCPLbRUx+iBE3V18nAkqiRdZodRcPvx2NirtajQdRW+gdtHkHSgsmWIMdBBJ7oHZF93S+G+z+LjO5R6wPuR0GyRMAzwMb7Oav+XAXpEKaiGij0KmQkhLMFxR2/olE+1QiI+3z12TlXA2DFFWk1yizLbawXHFBWgoQIDAQAB";
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(com.dingmk.comm.utils.Base64.decode(publicKey));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        RSAPublicKey publicK = (RSAPublicKey)keyFactory.generatePublic(x509KeySpec);

        byte[] data = new byte[]{127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,
                127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,
                127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,
                127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,
                127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,
                127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,
                127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,
                127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,
                127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,
                127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,
                127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,
                127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,
                127,127,127,127,127,127,127,127,127,127,127,127,127,127,127,127};

        BigInteger var2 = new BigInteger(1, data);
        System.out.println(var2.compareTo(publicK.getModulus()));
        System.out.println(var2);
        System.out.println(publicK.getModulus());
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey,
                                            String algorithm, int keyLength) throws Exception {
        byte[] decryptedData;
        if (null != algorithm) {
            byte[] keyBytes = Base64.decode(publicKey);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher;
            if (algorithm.endsWith("OAEPPadding")) {//第三方JCE
                cipher = Cipher.getInstance(algorithm, "BC");
            } else {
                cipher = Cipher.getInstance(algorithm);
            }
            cipher.init(Cipher.DECRYPT_MODE, publicK);
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            int decryptBlock = keyLength / 8;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > decryptBlock) {
                    cache = cipher.doFinal(encryptedData, offSet, decryptBlock);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * decryptBlock;
            }
            decryptedData = out.toByteArray();
            out.close();
        } else {
            throw new NullPointerException("解密的algorithm为空");
        }

        return decryptedData;
    }
}
