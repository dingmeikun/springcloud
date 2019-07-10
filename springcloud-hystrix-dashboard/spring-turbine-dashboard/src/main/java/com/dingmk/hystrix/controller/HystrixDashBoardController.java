package com.dingmk.hystrix.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HystrixDashBoardController {

	@GetMapping("/data/dashboard/query/v1")
    public String queryForList(@RequestParam String name) {

        return "I am " + name;
    }
	
}
