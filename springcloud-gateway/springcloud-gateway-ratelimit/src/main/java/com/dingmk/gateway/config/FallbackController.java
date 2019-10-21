package com.dingmk.gateway.config;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gateway")
public class FallbackController {

	@PostMapping("/fallback")
	public BasicLogicResult<String> fallback() {
		
		return new BasicLogicResult<String>(0, "Bad GateWay!");
	}
}
