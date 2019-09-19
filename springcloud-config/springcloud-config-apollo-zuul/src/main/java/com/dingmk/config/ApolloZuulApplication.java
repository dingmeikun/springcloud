package com.dingmk.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;

import java.io.IOException;

/**
 * @author dingmk
 * @date 2019-09-11 18:44:44
 */
@EnableApolloConfig
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class ApolloZuulApplication {

  public static void main(String[] args) throws IOException {

    SpringApplication.run(ApolloZuulApplication.class, args);
  }
}
