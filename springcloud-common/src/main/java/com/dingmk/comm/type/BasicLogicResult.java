package com.dingmk.comm.type;

import lombok.ToString;

/**
 * 逻辑层结果
 * create by Tony.Lau on 2017/06/13
 */
@ToString
public class BasicLogicResult<T extends Object> {
	
	public final int code;
	
	public final String msg;
	
	public final T obj;
	
	public BasicLogicResult() {
		this(0, null, null);
	}
	
	public BasicLogicResult(int code) {
		this(code, null, null);
	}
	
	public BasicLogicResult(int code, String msg) {
		this(code, msg, null);
	}

	public BasicLogicResult(int code, String msg, T obj) {
		this.code = code;
		this.msg = msg;
		this.obj = obj;
	}
	
}
