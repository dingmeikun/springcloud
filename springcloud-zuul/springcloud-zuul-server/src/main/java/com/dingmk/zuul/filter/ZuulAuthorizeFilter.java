package com.dingmk.zuul.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.dingmk.comm.constvar.ResultConstVar;
import com.dingmk.comm.type.BasicLogicResult;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ZuulAuthorizeFilter extends ZuulFilter{
	
	private static final BasicLogicResult<Object> INVALID_ARGUMENT = new BasicLogicResult<>(ResultConstVar.INVALID_ARGUMENT, "invalid token argument!");

	@Override
	public boolean shouldFilter() {
		return (boolean) RequestContext.getCurrentContext().get("Second-logic-isSuccess");
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String token = request.getHeader("token");
		
		// 获取请求中的参数
		log.info("Verify token :{}", token);
		if (!StringUtils.hasText(token)) {
			ctx.setSendZuulResponse(false); // 对该请求禁止路由，禁止请求下游服务
			ctx.setResponseBody(INVALID_ARGUMENT.toString());
			ctx.set("Second-logic-isSuccess", false); // 设置上下文信息，传递给下游服务/filter
			return null;
		}
		
		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 3;
	}

}
