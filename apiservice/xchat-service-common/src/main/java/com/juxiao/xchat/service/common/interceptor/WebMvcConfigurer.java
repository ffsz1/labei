package com.juxiao.xchat.service.common.interceptor;

import com.juxiao.xchat.base.spring.SpringAppContext;
import com.juxiao.xchat.service.common.aes.AesDecryptFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.servlet.Filter;

/**
 * @class: WebMvcConfigurer.java
 * @author: chenjunsheng
 * @date 2018/6/22
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurationSupport {
    @Value("${common.system.env}")
    private String env = "prod";

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        SignInterceptor signInterceptor = SpringAppContext.getBean(SignInterceptor.class);
        registry.addInterceptor(signInterceptor).addPathPatterns("/**");

        LoginInterceptor loginInterceptor = SpringAppContext.getBean(LoginInterceptor.class);
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        //if (!"prod".equalsIgnoreCase(env)) {
            registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
            registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        //}
        super.addResourceHandlers(registry);
    }

    @Bean
    public FilterRegistrationBean companyUrlFilterRegister() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        //注入过滤器
        registration.setFilter(new AesDecryptFilter());
        //拦截规则
        registration.addUrlPatterns("/*");
        //过滤器名称
        registration.setName("AesDecryptFilter");
        //过滤器顺序
        registration.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
        return registration;
    }
}
