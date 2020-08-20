package com.softplan.codechallenge.swagger;

import com.softplan.codechallenge.constants.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig implements WebMvcConfigurer {

    @Bean
    public Docket docketv1() {
        return new Docket(SWAGGER_2)
                .groupName("V1")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.softplan.codechallenge.web.v1"))
                .build()
                .apiInfo(infov1())
                .useDefaultResponseMessages(false)
                .tags(new Tag(Constants.PESSOAS_TAG, "Gerenciamento de pessoas"));
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
}
