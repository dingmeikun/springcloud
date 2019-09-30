package com.dingmk.zipkin.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "zipkin-zuul",url = "localhost:9709")
public interface ZipkinCLientService {

	@RequestMapping("/v1/sayHello")
    String sayHello(@RequestParam("name")String name);
}
