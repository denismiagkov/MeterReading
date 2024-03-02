package com.denmiagkov.meter.config;

import com.denmiagkov.meter.application.mapper.*;
import com.denmiagkov.meter.infrastructure.in.filter.AdminFilterWrapper;
import com.denmiagkov.meter.infrastructure.in.filter.UserFilterWrapper;
import com.denmiagkov.starter.logging.aspect.annotations.EnableLogging;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

/**
 * Конфигурация приложения
 */
@Configuration
@EnableLogging
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean<UserFilterWrapper.UserFilter> userFilter() {
        FilterRegistrationBean<UserFilterWrapper.UserFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new UserFilterWrapper.UserFilter());
        registrationBean.addUrlPatterns("/api/v1/user/*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AdminFilterWrapper.AdminFilter> adminFilter() {
        FilterRegistrationBean<AdminFilterWrapper.AdminFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AdminFilterWrapper.AdminFilter());
        registrationBean.addUrlPatterns("/api/v1/admin/*");
        return registrationBean;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .indentOutput(true);
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }
}
