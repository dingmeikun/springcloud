/*
 * PROJECT NAME: openplatform
 * CREATED TIME: 15-4-30 下午4:16
 *       AUTHOR: lizhiming
 *    COPYRIGHT: Copyright(c) 2015~2020 All Rights Reserved.
 *
 */
package com.dingmk.comm.type;

import com.dingmk.comm.constvar.ResultConstVar;

/**
 * @author lizhiming
 *         控制层返回消息基础接口，任何控制层返回消息必须实现该接口
 */
public interface BasicResponse {
    /**
     * 获取响应状态码
     *
     * @return
     */
    default int getStatus() {
        return ResultConstVar.ERROR;
    }

    /**
     * 获取响应状态描述信息
     *
     * @return
     */
    default String getDesc() {
        return null;
    }

    /**
     * 获取响应消息体
     *
     * @return
     */
    default Object getBody() {
        return null;
    }
}
