package com.dingmk.config.configure;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.context.annotation.Configuration;

/**
 * @author dingmk
 * @date 2019-09-10 17:44:44
 */
@Configuration
@EnableApolloConfig(value = "application", order = 10)
public class AppConfig {
}
