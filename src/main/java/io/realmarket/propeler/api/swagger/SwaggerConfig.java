package io.realmarket.propeler.api.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

import static io.realmarket.propeler.PropelerServiceApplication.PLATFORM_NAME;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .useDefaultResponseMessages(false)
        .select()
        .apis(RequestHandlerSelectors.basePackage("io.realmarket.propeler.api.controller"))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(getApiInfo())
        .enableUrlTemplating(false);
  }

  private ApiInfo getApiInfo() {
    return new ApiInfo(
        PLATFORM_NAME + "-backend REST API",
        String.format(
            "%s-backend REST API provides backend logic for the %s project.",
            PLATFORM_NAME, PLATFORM_NAME),
        "0.1.0-SNAPSHOT",
        "",
        null,
        "",
        "",
        Collections.emptyList());
  }
}
