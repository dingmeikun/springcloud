package com.dingmk.zuul.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dingmk.comm.constvar.ResultConstVar;
import com.dingmk.comm.type.BasicLogicResult;
import com.dingmk.zuul.service.WhiteIpService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ZuulWhiteIpFilter extends ZuulFilter{
	
	@Value("${sdkapi.application.ipfilter.enable:true}")
    private boolean checkIp;
	
	@Autowired
	private WhiteIpService ipService;
	
	private static final BasicLogicResult<Object> INVALID_ARGUMENT = new BasicLogicResult<>(ResultConstVar.INVALID_ARGUMENT, "invalid ip argument!");

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		if (!checkIp) {
            return null;
        }
		
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		
		String clientIp = request.getHeader("header");
        if (null == clientIp) {
            clientIp = request.getRemoteAddr();
        }
		
		// 白名单校验
		log.debug("Client IP :{}| CHAN:{}", clientIp, "test");
		if (!ipService.checkIp(clientIp)) {
            log.error("IP IS INVALID:{}|CHAN:{}", clientIp, "test");
            ctx.setSendZuulResponse(false); // 对该请求禁止路由，禁止请求下游服务
			ctx.setResponseBody(INVALID_ARGUMENT.toString());
			ctx.set("Second-logic-isSuccess", false); // 设置上下文信息，传递给下游服务/filter
			return null;
        }
		
		ctx.set("Second-logic-isSuccess", true);
		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 2;
	}

}
