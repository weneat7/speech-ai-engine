package com.cspl.common.gen_ai.speechaiengine.config;

import com.cspl.common.gen_ai.speechaiengine.filters.AuthFilter;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class FilterConfig {

//    @Bean
//    public FilterRegistrationBean<AuthFilter> authFilter(AuthFilter authFilter) {
//        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
//
//        registrationBean.setFilter(authFilter);
//        registrationBean.addUrlPatterns("/*"); // apply to all URLs
//        registrationBean.setOrder(1);
//
//        return registrationBean;
//    }
}

