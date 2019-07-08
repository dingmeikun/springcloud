package com.dingmk.comm.constvar;

/**
 * 标识类型
 * Created by cat on 2017/3/19.
 */
public class FlagConstVar {
    public static final short INVALID_FLAG = -1;

    /**
     * 压缩标识类型
     */
    public static final byte ZIP_FLAG_NO = 0;
    public static final byte ZIP_FLAG_GZIP = 1;

    /**
     * 加密标识类型
     */
    public static final byte CRYPT_FLAG_NO = 0;
    public static final byte CRYPT_FLAG_RSA_PUBLIC = 1;
    public static final byte CRYPT_FLAG_RSA_PRIVATE = 2;
    public static final byte CRYPT_FLAG_AES128 = 3;
    public static final byte CRYPT_FLAG_AES256 = 4;

    public static final byte CRYPT_FLAG_RSA_PUBLIC_OAEPPADDING = 5;
    public static final byte CRYPT_FLAG_RSA_PRIVATE_OAEPPADDING = 6;
}
