package com.dingmk.comm.type;

import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类似于C、C++语言中的结构体，封装多个属性，只保存数据用
 * 由应用使用（设置、读取），避免为不同的结构定义不同的类
 * SpringFramework中也有类似的对象MutablePropertyValues
 *
 * @author lizhiming on 2015/11/19.
 */
@Data
public class BasicPropertyValues {
    private Map<String, BasicPropertyValue> propertyValues;

    public BasicPropertyValues(BasicPropertyValue[] propValues) {
        this(Arrays.asList(propValues));
    }

    public BasicPropertyValues(List<BasicPropertyValue> propValues) {
        this.propertyValues = new HashMap<>();
        for (BasicPropertyValue it : propValues) {
            this.propertyValues.put(it.getName(), it);
        }
    }

    public Object getValue(String name) {
        if (name == null) {
            return null;
        }

        BasicPropertyValue propValue = this.propertyValues.get(name);
        if (propValue != null) {
            return propValue.getValue();
        } else {
            return null;
        }
    }

    public void addValue(String name, Object value) {
        /*if (this.getValue(name) != null) {
            throw new BasicException(ResultConstVar.KEY_DUPLICATE, "the name had been used.");
        }*/

        this.propertyValues.put(name, BasicPropertyValue.pack(name, value));
    }
}
