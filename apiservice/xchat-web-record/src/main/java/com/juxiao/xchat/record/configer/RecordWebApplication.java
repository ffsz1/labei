package com.juxiao.xchat.record.configer;

import com.juxiao.xchat.dao.config.DynamicDataSourceRegister;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@MapperScan("com.juxiao.xchat.dao")
@Import(DynamicDataSourceRegister.class)
@ComponentScan(basePackages = "com.juxiao.xchat")
@ServletComponentScan(basePackages = {"com.juxiao.xchat.service.common"})
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class RecordWebApplication extends SpringBootServletInitializer {

    /**
     * @see SpringBootServletInitializer#configure(SpringApplicationBuilder)
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(RecordWebApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(RecordWebApplication.class, args);
    }
}