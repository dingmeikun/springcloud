package com.dingmk.comm.utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {
	
	private static final char[] CHARS = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

	private static final int CHARS_SIZE = 36;
	
	/**
	 * <b>方法：</b>生成随机字符串</br>
	 * <b>说明：</b>MAC地址 + 16进制当前毫秒时间 + 随机字符 间隔排列生成随机字符串，适用于分布式集群。</br>
	 * <b>长度：</b>MAC地址(12) + 16进制时间(11) + 随机字符(12) = 35</br>
	 * <b>性能：</b>单线程生成1000万随机字符串约6秒
	 */
	public static String random(int count){
		char[] rs = new char[count];
		for(int i=0; i < count; i++){
		    int n = ThreadLocalRandom.current().nextInt(CHARS_SIZE);
			rs[i] = CHARS[n];
		}
		return new String(rs);
	}
	
	private RandomUtil(){}

}