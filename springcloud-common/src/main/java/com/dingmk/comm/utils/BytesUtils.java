/*
 * Copyright 2017 Tony.lau All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dingmk.comm.utils;

import java.util.Arrays;

/**
 * @author Tony.Lau
 * @blog: https://my.oschina.net/xcafe
 * @createTime 2017-03-01 05:09:16
 */
public class BytesUtils {

	public static final byte[] EMPTY_BYTES = new byte[0];

	public static final byte[] ONE_BYTES = new byte[] { 1 };
	
	public static final byte[] NULL_VALUE_BYTES = new byte[] {123, 125};
	
	public static final int NULL_VALUE_LENGTH = NULL_VALUE_BYTES.length;

	public static boolean isNullValue(byte[] bytes) {
		if (null == bytes) {
			return false;
		}
		
		if (NULL_VALUE_BYTES == bytes) {
			return true;
		}
		
		if (NULL_VALUE_LENGTH == bytes.length) {
			for (int i = 0; i < NULL_VALUE_LENGTH; i++) {
				if(bytes[i] != NULL_VALUE_BYTES[i]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public static boolean isEmpty(byte[] bytes) {
		return (null == bytes || bytes.length == 0);
	}

	public static byte[] merge(byte[] src1, byte[] src2) {
		if (null == src1 || src1.length == 0) {
            return src2;
        }
		if (null == src2 || src2.length == 0) {
            return src1;
        }

		int alen = src1.length;
		int blen = src2.length;
		byte[] dest = Arrays.copyOf(src1, alen + blen);
		System.arraycopy(src2, 0, dest, alen, blen);
		return dest;
	}

	private BytesUtils() {
	}
}
