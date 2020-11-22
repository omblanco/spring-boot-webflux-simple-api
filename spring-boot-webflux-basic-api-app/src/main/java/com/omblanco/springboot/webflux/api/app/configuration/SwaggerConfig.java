package com.omblanco.springboot.webflux.api.app.configuration;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.omblanco.springboot.webflux.api.app.utils.BaseApiConstants;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * @author oscar.martinezblanco
 *
 * see http://springfox.github.io/springfox/docs/snapshot/#springfox-spring-data-rest
 * http://localhost:8080/swagger-ui/index.html
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${app.version}")
    private String appVersion;
    
    @Bean
    public Docket docketUsersV1() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.usersApiInfoV1())
                .enable(true)
                .groupName("user-api-v1")
                .select()
                .paths(userPathsV1())
                .build();
    }
    
    private ApiInfo usersApiInfoV1() {
        return new ApiInfoBuilder()
                .title("Reactive Users V1")
                .description("Reactive API V1 para usuarios desarrollada con @Controller")
                .version(appVersion)
                .build();
    }

    
    private Predicate<String> userPathsV1() {
        return regex(BaseApiConstants.USER_BASE_URL_V1.concat(".*"));
    }
    
    @Bean
    public Docket docketUsersV2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.usersApiInfoV2())
                .enable(true)
                .groupName("user-api-v2")
                .select()
                .paths(userPathsV2())
                .build();
    }
    
    private ApiInfo usersApiInfoV2() {
        return new ApiInfoBuilder()
                .title("Reactive Users V2")
                .description("Reactive API V2 para usuarios desarrollada con @RestController")
                .version(appVersion)
                .build();
    }

    
    private Predicate<String> userPathsV2() {
        return regex(BaseApiConstants.USER_BASE_URL_V2.concat(".*"));
    }
    
    @Bean
    public Docket docketUsersV3() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.usersApiInfoV3())
                .enable(true)
                .groupName("user-api-v3")
                .select()
                .paths(userPathsV3())
                .build();
    }
    
    private ApiInfo usersApiInfoV3() {
        return new ApiInfoBuilder()
                .title("Reactive Users V3")
                .description("Reactive API V3 para usuarios desarrollada con Functional Endpoints")
                .version(appVersion)
                .build();
    }

    
    private Predicate<String> userPathsV3() {
        return regex(BaseApiConstants.USER_BASE_URL_V3.concat(".*"));
    }
    
    @Bean
    public Docket docketAppInfo() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.appInfoapiInfo())
                .enable(true)
                .groupName("app-info-api")
                .select()
                .paths(AppInfoPaths())
                .build();
    }
    
    private ApiInfo appInfoapiInfo() {
        return new ApiInfoBuilder()
                .title("Reactive App Info")
                .description("Reactive API V1 para recuperar la información de la aplicación desarrollada con @RestController")
                .version(appVersion)
                .build();
    }

    private Predicate<String> AppInfoPaths() {
        return regex(BaseApiConstants.STATUS_BASE_URL_V1.concat(".*"));
    }
}