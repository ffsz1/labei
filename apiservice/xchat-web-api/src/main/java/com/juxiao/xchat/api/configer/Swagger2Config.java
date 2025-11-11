package com.juxiao.xchat.api.configer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2 配置类
 *
 * @class: Swagger2Config.java
 * @author: chenjunsheng
 * @date 2018/8/3
 */
@Configuration
@Slf4j
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("官方api接口文档")
                        .description("所有需要ticket的接口，都需要上传uid和ticket；所有需要加密的接口，都需要上传os、appVersion、t、sn四个字段")
                        .version("1.0")
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.juxiao.xchat.api"))
                .paths(PathSelectors.any())
                .build();
    }
}
