package com.dingmk.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * @author dingmk
 * @date 2019-09-10 17:44:44
 */
@SpringBootApplication
public class ApolloClientApplication {

  public static void main(String[] args) throws IOException {

    SpringApplication.run(ApolloClientApplication.class, args);
  }
}
