package com.dingmk.eureka.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dingmk.eureka.config.BaseRequest;
import com.dingmk.eureka.config.BaseResponse;
import com.dingmk.eureka.feign.config.FeignConfig;
import com.dingmk.eureka.hystrix.BaseHystrix;

@FeignClient(value = "eureka-service", configuration = FeignConfig.class, fallback = BaseHystrix.class)
public interface BaseServiceFeign {

	@RequestMapping(value = "/data/service/query/v1", method = RequestMethod.POST)
    BaseResponse queryLocation(@RequestBody BaseRequest request);
}
