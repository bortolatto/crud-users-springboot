package com.softplan.codechallenge.swagger;

import com.softplan.codechallenge.constants.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig implements WebMvcConfigurer {

    @Value("${uaa.clientId}")
    String clientId;
    @Value("${uaa.clientSecret}")
    String clientSecret;

    @Value("${uaa.url}")
    String oAuthServerUri;

    @Bean
    public Docket docketv1() {
        return new Docket(SWAGGER_2)
                .groupName("V1")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.softplan.codechallenge.web.v1"))
                .build()
                .apiInfo(infov1())
                .useDefaultResponseMessages(false)
                .tags(new Tag(Constants.PESSOAS_TAG, "Gerenciamento de pessoas"))
                .securitySchemes(Collections.singletonList(oauth()));
    }

    @Bean
    List<GrantType> grantTypes() {
        List<GrantType> grantTypes = new ArrayList<>();
        TokenEndpoint tokenEndpoint = new TokenEndpoint(oAuthServerUri+"/oauth/token", "token");
        grantTypes.add(new ClientCredentialsGrant(tokenEndpoint.getUrl()));



        return grantTypes;
    }

    @Bean
    SecurityScheme oauth() {
        return new OAuthBuilder()
                .name("OAuth2")
                .grantTypes(grantTypes())
                .build();
    }

    @Bean
    public Docket docketCommons() {
        return new Docket(SWAGGER_2)
                .groupName("Commons")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.softplan.codechallenge.web"))
                .paths(regex("/source"))
                .build()
                .apiInfo(infov1())
                .useDefaultResponseMessages(false)
                .tags(new Tag(Constants.GIT_REPOSITORY_TAG, "Consulta repositório do projeto"));
    }

    @Bean
    public Docket docketv2() {
        return new Docket(SWAGGER_2)
                .groupName("V2")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.softplan.codechallenge.web.v2"))
                .build()
                .apiInfo(infov2())
                .useDefaultResponseMessages(false)
                .tags(new Tag(Constants.PESSOAS_TAG, "Gerenciamento de pessoas"));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    public ApiInfo infov1() {
        return new ApiInfoBuilder()
                .title("Softplan API")
                .description("API pública de cadastro de pessoas")
                .version("1")
                .contact(new Contact("Softplan", "http://www.softplan.com.br", "softplan@softplan.com.br"))
                .build();
    }

    public ApiInfo infov2() {
        return new ApiInfoBuilder()
                .title("Softplan API")
                .description("API pública de cadastro de pessoas")
                .version("2")
                .contact(new Contact("Softplan", "http://www.softplan.com.br", "softplan@softplan.com.br"))
                .build();
    }

    @Bean
    public SecurityConfiguration securityInfo() {
        return new SecurityConfiguration(clientId, clientSecret, "realm", "Softplan Code Challenge", "apiKey", ApiKeyVehicle.HEADER, "api_key", "");
    }


}
