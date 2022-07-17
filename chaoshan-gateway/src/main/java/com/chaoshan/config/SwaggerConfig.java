package com.chaoshan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @DATE: 2022/05/05 09:58
 * @Author: 小爽帅到拖网速
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
  @Bean
  public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
            // .securitySchemes(Collections.singletonList(securitySchemes()))
            // .securityContexts(Collections.singletonList(securityContexts()))
            .enable(true)
            .apiInfo(apiInfo());
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
            .title("潮在文汕Api接口测试文档")
            .description("潮在文汕Api接口文档说明")
            .termsOfServiceUrl("http://localhost:8001")
            .contact(new Contact("chaoshan", "https://blog.csdn.net/weixin_46195957?type=blog", "1372713212@qq.com"))
            .version("1.0")
            .build();
  }

  @Bean
  UiConfiguration uiConfig() {
    return new UiConfiguration(null, "list", "alpha", "schema",
            UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, false, true, 60000L);
  }

  // /**
  //  * 认证方式使用密码模式
  //  */
  // private SecurityScheme securitySchemes() {
  //   GrantType grantType = new ResourceOwnerPasswordCredentialsGrant("/oauth/token");
  //   return new OAuthBuilder()
  //           .name("Authorization")
  //           .grantTypes(Collections.singletonList(grantType))
  //           .scopes(Arrays.asList(scopes()))
  //           .build();
  // }
  //
  // /**
  //  * 设置 swagger2 认证的安全上下文
  //  */
  // private SecurityContext securityContexts() {
  //   return SecurityContext.builder()
  //           .securityReferences(Collections.singletonList(new SecurityReference("Authorization", scopes())))
  //           // .operationSelector((each) -> true)
  //           .build();
  // }
  //
  // /**
  //  * 允许认证的scope
  //  */
  // private AuthorizationScope[] scopes() {
  //   AuthorizationScope authorizationScope = new AuthorizationScope("ROLE_API", "accessEverything");
  //   AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
  //   authorizationScopes[0] = authorizationScope;
  //   return authorizationScopes;
  // }

}
