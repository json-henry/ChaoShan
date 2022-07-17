package com.chaoshan.config;

import com.chaoshan.util.anno.MyAnnotationSwagger;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

/**
 * @DATE: 2022/05/05 10:07
 * @Author: 小爽帅到拖网速
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        //添加head参数配置start
        return new Docket(DocumentationType.SWAGGER_2)

                // .enable(true)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.chaoshan"))
                .apis(Predicates.not( // 取反
                        RequestHandlerSelectors.withMethodAnnotation( // 当方法上有MyAnnotationSwagger注解时，返回true
                                MyAnnotationSwagger.class)))
                .build()
                .securityContexts(Arrays.asList(securityContexts()))
                .securitySchemes(Arrays.asList(securitySchemes()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("article模块")
                .description("article模块模块接口API文档")
                .contact(new Contact("小爽帅到拖网速", "https://blog.csdn.net/weixin_46195957?type=blog", "1372713212@qq.com"))
                .version("2.0")
                .build();
    }

    private SecurityScheme securitySchemes() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private SecurityContext securityContexts() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("xxx", "描述信息");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }
}
