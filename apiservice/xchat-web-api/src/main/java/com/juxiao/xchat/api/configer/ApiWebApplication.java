package com.juxiao.xchat.api.configer;

import com.juxiao.xchat.dao.config.DynamicDataSourceRegister;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 服务启动入口类
 *
 * @class: ApiWebApplication.java
 * @author: chenjunsheng
 * @date 2018/6/6
 */
@EnableAsync
@EnableSwagger2
@EnableAutoConfiguration
@EnableTransactionManagement
@Configuration
@MapperScan("com.juxiao.xchat.dao")
@Import(DynamicDataSourceRegister.class)
@ComponentScan(basePackages = "com.juxiao.xchat")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@Slf4j
public class ApiWebApplication extends SpringBootServletInitializer {
    /**
     * @see SpringBootServletInitializer#configure(SpringApplicationBuilder)
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ApiWebApplication.class);
    }

    public static void main(String[] args) {
        try {
            SpringApplication.run(ApiWebApplication.class, args);
        } catch (Exception e){
            log.error(e.getMessage());
        }
    }
}



