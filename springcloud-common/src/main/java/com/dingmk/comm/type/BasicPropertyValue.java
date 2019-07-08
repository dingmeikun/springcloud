package com.dingmk.comm.type;

import lombok.Data;

/**
 * @author lizhiming on 2015/11/19.
 */
@Data
public class BasicPropertyValue {
    /**
     * 属性名
     */
    private final String name;

    /**
     * 属性值
     */
    private final Object value;

    /**
     * 构造函数
     *
     * @param name
     * @param value
     */
    public BasicPropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 包装函数
     *
     * @param name
     * @param value
     * @return
     */
    public static BasicPropertyValue pack(String name, Object value) {
        return new BasicPropertyValue(name, value);
    }
}
