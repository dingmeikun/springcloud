package com.dingmk.config.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RefreshScope
@Component
@ConfigurationProperties(prefix = "com.dingmk.spring")
public class ClinetConfigGitProperties {

	private String config;
}
