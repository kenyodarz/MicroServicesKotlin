package com.bykenyodarz.mskotlin.usuarios.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ApiKey
import springfox.documentation.service.Contact
import springfox.documentation.service.SecurityScheme
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


@Configuration
@EnableSwagger2
class SwaggerConfiguration {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.bykenyodarz.mskotlin.usuarios.controllers"))
            .build()
            .apiInfo(apiInfo())
            .securitySchemes(listOf<SecurityScheme>(apiKey()))
    }

    private fun apiKey(): ApiKey {
        return ApiKey("jwtToken", "Authorization", "header")
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("Api de ejemplo de JWT")
            .description("Rest API")
            .termsOfServiceUrl("localhost")
            .version("1.0")
            .contact(
                Contact(
                    "Jorge Mina", "https://personalsoft.com",
                    "jomina@personalsoft.com.co"
                )
            )
            .build()
    }
}