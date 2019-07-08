/*
 * PROJECT NAME: openplatform
 * CREATED TIME: 15-4-30 下午4:16
 *       AUTHOR: lizhiming
 *    COPYRIGHT: Copyright(c) 2015~2020 All Rights Reserved.
 *
 */

package com.dingmk.comm.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

import com.dingmk.comm.constvar.SysConstVar;

import lombok.extern.slf4j.Slf4j;

/**
 * 字符串工具类
 *
 * @author lizhiming
 */
@Slf4j
public final class StringUtil {

    private static final char[] hexDigitsUC = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final char[] hexDigitsLC = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	
    private static final Pattern p = Pattern.compile("[\\r\\t\\n]*", Pattern.CASE_INSENSITIVE);
    
    /**
     * 工具类禁止实例化
     */
    private StringUtil() {
    }

    public static String[] toNotNullArray(String str, String reg) {
        if (null != str && !(str = str.trim()).isEmpty()) {
            String[] array = str.split(reg);
            int length = array.length;
            List<String> list = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                String temp = array[i];
                if (null != temp && !(temp = temp.trim()).isEmpty()) {
                    list.add(temp);
                }
            }
            int size = list.size();
            array = new String[size];
            array = list.toArray(array);
            return array;
        }
        return new String[0];
    }


    /**
     * 将bytes转成String
     *
     * @param bytes
     * @return
     */
    public static String fromBytes(byte[] bytes) {
    	if(null != bytes){
            try {
                return new String(bytes, SysConstVar.DEFAULT_CHARSET);
            } catch (UnsupportedEncodingException e) {
                log.error("StringUtil fromBytes meet execption. " + new String(bytes, StandardCharsets.UTF_8));
            }
    	}
        return null;
    }

    /**
     * 将String转成Bytes
     *
     * @param str
     * @return
     */
    public static byte[] toBytes(String str) {
        if(null != str){
        	try {
                return str.getBytes(SysConstVar.DEFAULT_CHARSET);
            } catch (UnsupportedEncodingException e) {
                log.error("StringUtil toBytes meet execption. " + str);
            }
        }
        return null;
    }

    /**
     * byte[]转16进制字符串(默认小写)
     * @param byteArray byte数组
     * @return 转换结果
     */
    public static String byteToHexStr(byte[] byteArray) {
    	return byteToHexStr(true, byteArray);
    }
    
	/**
	 * byte[]转16进制字符串
	 * @param toLowerCase
	 * @param digest
	 * @return
	 */
	public static String byteToHexStr(boolean toLowerCase, byte[] digest){
		//判断大小写
        char[] hexDigits = hexDigitsUC; 
        if(toLowerCase){
        	hexDigits = hexDigitsLC;
        }
        //转换成16进制
        int length = digest.length;
        char[] chars = new char[length * 2];
        int k = 0;
        for (int i = 0; i < length; i++) {
            byte byte0 = digest[i];
            chars[k++] = hexDigits[byte0 >>> 0x4 & 0xf];
            chars[k++] = hexDigits[byte0 & 0xf];
        }
		return new String(chars);
	}

    /**
     * BASE64编码
     * variant of Base64 (see RFC 3548 section 4) where - and _ are used in place of + and /.
     *
     * @param bytes 待编码内容
     * @return 编码后的结果
     */
    public static byte[] base64Encode(final byte[] bytes) {
        return Base64.encodeBase64URLSafe(bytes);
    }

    /**
     * BASE64解码
     *
     * @param bytes 待解码内容
     * @return 解码后的结果
     */
    public static byte[] base64Decode(final byte[] bytes) {
        return Base64.decodeBase64(bytes);
    }

    /**
     * 将资金金额对象转成字符串
     *
     * @param amount 资金对象
     * @return 结果
     */
    public static String decimal2String(BigDecimal amount) {
        return (amount == null) ? "0.00" : amount.toString();
    }

    /**
     * 判断字符串是否有效<br>
     * true：字符串不等于空且去除空白字符后长度大于0； false：字符串为空或者去除空白后长度为0
     * @param str 待检查字符串
     * @return true:有效；false:无效
     */
    public static boolean isValidString(String str) {
        return (str != null && str.trim().length() != 0);
    }

    /**
     * 将一个数字用指定的符号分隔。从后往后分隔。例如：10005 <==> 10/005
     *
     * @param num     需要分隔的数字
     * @param pattern 分隔的字符
     * @param per     相隔间距
     * @return 处理后的结果
     */
    public static String insertString(long num, String pattern, int per) {
        final StringBuffer sb = new StringBuffer(String.valueOf(num));
        int len = sb.length();
        while (len >= per) {
            final int offset = len - per;
            sb.insert(offset, pattern);
            len = len - per;
        }

        if (len > 0) {
            sb.insert(0, pattern);
        }

        sb.append(pattern);

        return sb.toString();
    }

    /**
     * 将字符数组以16进制形式dump出来
     *
     * @param byteArray 字符数组
     */
    public static void dumpByteArray(byte[] byteArray) {
        log.debug("printByteArray begin : ");
        for (int j = 0; j < byteArray.length; j++) {
            String hex = Integer.toHexString(byteArray[j] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            log.debug("0x" + hex);
        }
        log.debug("printByteArray end.");
    }

    /**
     * 去掉文本当中的回车换行符和table，用空格替代
     *
     * @param input 输入参数
     * @return 处理后的结果
     */
    public static String delReturnLineChar(String input) {
        if (!StringUtils.hasText(input)) {
            return input;
        }

        Matcher m = p.matcher(input);
        String out = m.replaceAll("");
        return out;
    }


    /**
     * 将字符串倒数第6个字符起，直到倒数第2个字符之间用****代替
     * 如：18689****23，458972198910****70
     *
     * @param str 输入字符串
     * @return 处理后的字符串
     */
    public static String shieldString(String str) {
        if (StringUtils.hasText(str) == false) {
            return "";
        }

        if (str.length() < 8) {
            return str;
        }
        final int shieldbeginpos = 6;
        final int shieldendpos = 2;

        StringBuilder sb = new StringBuilder(str);
        int len = sb.length();
        int start = len - shieldbeginpos;
        int end = len - shieldendpos;
        if (start <= 0 || end <= 0) {
            return "****";
        }
        return sb.replace(len - shieldbeginpos, len - shieldendpos, "****").toString();
    }
}
