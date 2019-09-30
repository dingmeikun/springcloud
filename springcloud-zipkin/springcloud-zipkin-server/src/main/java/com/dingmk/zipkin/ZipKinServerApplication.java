package com.dingmk.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import zipkin.server.EnableZipkinServer;

@EnableZipkinServer
@SpringBootApplication
public class ZipKinServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZipKinServerApplication.class, args);
	}
}
