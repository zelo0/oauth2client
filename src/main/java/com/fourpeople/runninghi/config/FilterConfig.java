package com.fourpeople.runninghi.config;

import com.fourpeople.runninghi.filter.MyFilter1;
import com.fourpeople.runninghi.filter.MyFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
//
//    @Bean
//    public FilterRegistrationBean<MyFilter1> filter1() {
//        FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>(new MyFilter1());
//        bean.addUrlPatterns("/*"); // 모든 url에서 필터 사용
//        bean.setOrder(0);   // 우선순위가 낮을수록 먼저 실행되는 필터
//        return bean;
//    }

    @Bean
    public FilterRegistrationBean<MyFilter2> filter2() {
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
        bean.addUrlPatterns("/*"); // 모든 url에서 필터 사용
        bean.setOrder(1);   // 우선순위가 낮을수록 먼저 실행되는 필터
        return bean;
    }

}
