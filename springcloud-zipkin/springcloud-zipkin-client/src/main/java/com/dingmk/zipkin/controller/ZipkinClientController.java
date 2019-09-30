package com.dingmk.zipkin.controller;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.dingmk.zipkin.service.ZipkinCLientService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ZipkinClientController {

	@Autowired
    private ZipkinCLientService helloService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ExecutorService executorService;

    @GetMapping("/helloByFeign")
    public String helloByFeign(@RequestParam("name")String name){
        log.info("client sent. Feign 方式, 参数: {}",name);

        String result = helloService.sayHello(name);

        log.info("client received. Feign 方式, 结果: {}",result);
        return result;
    }

    @GetMapping("/helloByRestTemplate")
    public String helloByRestTemplate(@RequestParam("name")String name){
        log.info("client sent. RestTemplate方式, 参数: {}",name);

        String url = "http://localhost:9709/v1/sayHello?name="+name;
        String result = restTemplate.getForObject(url,String.class);

        log.info("client received. RestTemplate方式, 结果: {}",result);
        return result;
    }

    @GetMapping("/helloByNewThread")
    public String hello(@RequestParam("name")String name) throws ExecutionException, InterruptedException {
        log.info("client sent. 子线程方式, 参数: {}",name);

        Future<String> future = executorService.submit(() -> {
            log.info("client sent. 进入子线程, 参数: {}",name);
            String result = helloService.sayHello(name);
            return result;
        });
        String result = future.get();
        log.info("client received. 返回主线程, 结果: {}",result);
        return result;
    }
}
