package com.dingmk.eureka.config;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 这个比较关键，turbine的收集默认路径是：/hystrix.stream，而springboot2.0.6版本的默认路径不是这个，所以需要重新制定路径
 * 
 * 修改方式：配置Bean，设置路径/actuator/hystrix.stream，并且要在turbine服务的application.yml文件制定该参数
 * 
 * @author dingmeikun
 *
 */
@Configuration
public class HystrixConfiguration {
    @Bean
    public ServletRegistrationBean getServlet() {
        HystrixMetricsStreamServlet hystrixMetricsStreamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(hystrixMetricsStreamServlet);
        servletRegistrationBean.addUrlMappings("/actuator/hystrix.stream");
        servletRegistrationBean.setName("HystrixMetricsStreamServlet");
        return servletRegistrationBean;
    }
}
