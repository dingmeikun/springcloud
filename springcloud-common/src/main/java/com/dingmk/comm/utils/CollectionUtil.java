package com.dingmk.comm.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionUtil {
	
	public static <K, V> Map<K, V> listToMap(List<Map<K, V>> list){
		if(isEmpty(list)) {
			return null;
		}
		
		Map<K, V> tagMap = new HashMap<K, V>();
    	for(Map<K, V> map : list) {
    		if(null != map) {
    			tagMap.putAll(map);
    		}
    	}
    	
    	return tagMap;
	}
    
    public static <K, V> Map<K, V> listToMap(List<K> list, Class<V> valueType){
        if(null == list || list.isEmpty()){
            return null;
        }
        
        Map<K, V> map = new HashMap<>();
        for(K k : list){
            map.put(k, null);
        }
        
        return map;
    }
    
    public static <K> List<K> mapToList(Map<K, ?> map){
        if(isEmpty(map)){
            return null;
        }
        return setToList(map.keySet());
    }
    
    public static <K> List<K> setToList(Set<K> set){
        if(isEmpty(set)){
            return null;
        }
        List<K> list = new LinkedList<>();
        list.addAll(set);
        return list;
    }
    
    public static <K> boolean isEmpty(Collection<K> collection){
        return (null == collection || collection.isEmpty());
    }
    
    public static boolean isEmpty(Map<?, ?> map){
        return (null == map || map.isEmpty());
    }

}
