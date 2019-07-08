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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

/**
 * @author Tony.Lau
 * @blog: https://my.oschina.net/xcafe
 * @createTime 2017-03-02 18:28:37
 */
public class BeanUtils {
	
	public static Object getBeanProperty(Object bean, String fieldName){
		Assert.notNull(bean, "value must not be null");
		try{
			Field f = bean.getClass().getDeclaredField(fieldName);
			f.setAccessible(true);
			return f.get(bean);
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> getBeansProperty(List<?> list, String fieldName, Class<T> type){
		Assert.notNull(list, "beanList must not be null");
		int length = list.size();
		if(length>0){
			List<T> idlist = new ArrayList<T>();
			for(int i=0; i<length; i++){
				idlist.add((T)getBeanProperty(list.get(i), fieldName));
			}
			return idlist;
		}
		return null;
	}
	
}
