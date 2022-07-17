package com.chaoshan;

import com.chaoshan.common.SwaggerServers;
import com.chaoshan.common.entity.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @DATE: 2022/04/28 18:14
 * @Author: 小爽帅到拖网速
 */
@SpringBootApplication
@EnableZuulProxy
@ComponentScan("com.chaoshan")
public class GatewayApplication {
  public static void main(String[] args) {
    ConfigurableApplicationContext applicationContext = SpringApplication.run(GatewayApplication.class, args);
    // System.out.println(applicationContext.getBean("documentationConfig").toString());
  }

  @Component
  @Primary
  class DocumentationConfig implements SwaggerResourcesProvider {

    @Override
    public List<SwaggerResource> get() {
      List resources = new ArrayList();
      for (Server server : SwaggerServers.getServers()) {
        resources.add(swaggerResource(server.getName(), server.getLocation(), server.getVersion()));
      }


      return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
      SwaggerResource swaggerResource = new SwaggerResource();
      swaggerResource.setName(name);
      swaggerResource.setLocation(location);
      swaggerResource.setSwaggerVersion(version);
      return swaggerResource;
    }
  }
}
