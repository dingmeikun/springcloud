package com.dingmk.gateway.hystrix;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dingmk.gateway.route.common.BasicLogicResult;

@RestController
@RequestMapping("/gateway")
public class FallbackController {

	@PostMapping("/fallback")
	public BasicLogicResult<String> fallback() {
		return new BasicLogicResult<String>(0, "Bad GateWay!");
	}
}
