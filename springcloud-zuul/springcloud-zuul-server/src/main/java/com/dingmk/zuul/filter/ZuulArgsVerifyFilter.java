package com.dingmk.zuul.filter;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.alibaba.fastjson.JSONObject;
import com.dingmk.comm.constvar.ResultConstVar;
import com.dingmk.comm.type.BasicLogicResult;
import com.dingmk.zuul.config.BaseRequest;
import com.dingmk.zuul.service.WhiteIpService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ZuulArgsVerifyFilter extends ZuulFilter {
	
	@Autowired
	private WhiteIpService ipService;
	
	private static final BasicLogicResult<Object> INVALID_ARGUMENT = new BasicLogicResult<>(ResultConstVar.INVALID_ARGUMENT, "invalid ip argument!");

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		
		BaseRequest baseRequest = new BaseRequest();
		try {
			ServletInputStream in = request.getInputStream();
			String reqBody = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
			baseRequest = JSONObject.parseObject(reqBody, BaseRequest.class);
		} catch (IOException e) {
			log.error("execute second filter meet error! message {}", e.getMessage());
			e.printStackTrace();
		}
		
		// 参数校验
		String remoteIp = baseRequest.getIp();
		if (ipService.ipCheck(remoteIp)) {
			log.error("Params[IP] IS INVALID: {}", remoteIp);
			ctx.setSendZuulResponse(false); // 对该请求禁止路由，禁止请求下游服务
			ctx.setResponseBody(INVALID_ARGUMENT.toString());
		}
		
		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 4;
	}
    
}
