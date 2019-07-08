package com.dingmk.comm.type;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lizhiming on 2015/11/11.
 */
@Getter
@Setter
public class ExtLogicParam extends BasicLogicParam {
    /**
     * 包装其他请求参数
     */
    protected final Map<String, Object> paramsMap = new HashMap<>();

    /**
     * 附加参数
     *
     * @param paramName  参数名
     * @param paramValue 参数值
     * @return
     */
    public Object addObject(String paramName, Object paramValue) {
        return paramsMap.put(paramName, paramValue);
    }

    /**
     * 获取参数
     *
     * @param paramName 参数名
     * @return
     */
    public Object getObject(String paramName) {
        return paramsMap.get(paramName);
    }
}
