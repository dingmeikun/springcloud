/*
 * PROJECT NAME: openplatform
 * CREATED TIME: 15-4-30 下午4:16
 *       AUTHOR: lizhiming
 *    COPYRIGHT: Copyright(c) 2015~2020 All Rights Reserved.
 *
 */
package com.dingmk.comm.type;

/**
 * @author lizhiming
 *         封装系统异常
 */
public class BasicException extends RuntimeException {
    
    private static final long serialVersionUID = 2578218617690981957L;

    /**
     * 异常消息描述
     * 该描述可能会返回给客户端，需以客户端能理解的角度描述该异常，请注意信息描述的准确性和规范性
     */
    private String message;

    /**
     * 错误码
     */
    private int errorCode;

    public BasicException(int errorCode) {
        this.errorCode = errorCode;
    }

    public BasicException(int errorCode, String message) {
        super(message);

        this.message = message;
        this.errorCode = errorCode;
    }

    public BasicException (int errorCode, String message, Exception cause) {
        super(message, cause);
        this.message = message;
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
