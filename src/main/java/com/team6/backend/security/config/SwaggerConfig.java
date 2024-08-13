package com.team6.backend.security.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("springdoc-public")
                .pathsToMatch("/**")
                .addOpenApiCustomizer(loginEndpointCustomizer())
                .build();
    }

    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
                .version("v1.0.0")
                .title("Encore Hub API")
                .description("Encore Hub API 목록입니다.");


        return new OpenAPI()
                .info(info);
    }

    @Bean
    public OpenApiCustomizer loginEndpointCustomizer() {
        return openApi -> {
            Operation operation = new Operation()
                    .description("이메일과 비밀번호를 입력해 로그인합니다. 로그인에 성공하면 200을, 로그인에 실패하면 401 에러를 반환합니다.");
            Schema<?> schema = new ObjectSchema()
                    .addProperties("email", new StringSchema())
                    .addProperties("password", new StringSchema());
            RequestBody requestBody = new RequestBody()
                    .content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, new MediaType().schema(schema)));
            operation.requestBody(requestBody);

            ApiResponses apiResponses = new ApiResponses();
            apiResponses.addApiResponse("200", new ApiResponse().description("OK"));
            apiResponses.addApiResponse("401", new ApiResponse().description("Unauthorized"));
            operation.responses(apiResponses);

            operation.addTagsItem("member-controller");

            openApi.getPaths().addPathItem("/api/member/login", new PathItem().post(operation));
        };
    }
}