package com.dingmk.config.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@RefreshScope
public class ClinetConfigGitProperties {

	@Value("${com.dingmk.spring.config}")
	private String config;
}
