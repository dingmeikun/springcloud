/*
 *  PROJECT NAME: openplatform
 *  CREATED TIME: 15-4-30 下午10:25
 *        AUTHOR: lizhiming
 *     COPYRIGHT: Copyright(c) 2015~2020 All Rights Reserved.
 */

package com.dingmk.comm.type;

import com.dingmk.comm.constvar.SysConstVar;
import com.dingmk.comm.constvar.TypeConstVar;

import lombok.Getter;
import lombok.Setter;

/**
 * @author by lizhiming on 15/4/30.
 */
@Getter
@Setter
public class BasicLogicParam {
    /**
     * 用户ID
     */
    protected long userPId = SysConstVar.INVALID_ID;

    /**
     * 请求来源类型
     */
    protected short fromType = TypeConstVar.Client_Type_UNKNOWN;

    /**
     * 请求来源内容，对于网络请求该字段格式为IP:Port
     */
    protected String fromContent;

    /**
     * 构造函数
     */
    public BasicLogicParam() {
    }

    public BasicLogicParam(long userPId) {
        this.userPId = userPId;
    }


    /**
     * 拷贝构造函数
     *
     * @param logicParam 拷贝对象
     */
    public BasicLogicParam(BasicLogicParam logicParam) {
        this.userPId = logicParam.getUserPId();
        this.fromType = logicParam.getFromType();
        this.fromContent = logicParam.getFromContent();
    }

    /**
     * 检查参数是否有效，子类需自己重载该方法
     *
     * @return 参数是否有效
     */
    public boolean isValidParam() {
        if (this.userPId < 0) {
            return false;
        }

        return true;
    }
}
