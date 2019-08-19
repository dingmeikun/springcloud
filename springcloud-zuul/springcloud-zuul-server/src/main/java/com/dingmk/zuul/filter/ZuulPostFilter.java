package com.dingmk.zuul.filter;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class ZuulPostFilter extends ZuulFilter{

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		ctx.getResponse().setCharacterEncoding("UTF-8");
		
		String responseBody = ctx.getResponseBody();
		if (StringUtils.hasText(responseBody)) {
			ctx.setResponseStatusCode(500);
			ctx.setResponseBody(responseBody);
		}
		
		return null;
	}

	@Override
	public String filterType() {
		return "post";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

}
