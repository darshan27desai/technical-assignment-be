package com.test.addressbook.config;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.springmvc.OpenApiValidationFilter;
import com.atlassian.oai.validator.springmvc.OpenApiValidationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.io.IOException;

@Configuration
public class OpenApiValidationConfig implements WebMvcConfigurer {


    Boolean requestValidation;
    Boolean responseValidation;

    private final OpenApiValidationInterceptor validationInterceptor;
    @Autowired
    public OpenApiValidationConfig(@Value("${openapi.contract.path}") String contractPath,
                                   @Value("${openapi.request.validation}") Boolean requestValidation,
                                   @Value("${openapi.response.validation}") Boolean responseValidation) throws IOException {

        this.validationInterceptor = new OpenApiValidationInterceptor(OpenApiInteractionValidator
                .createFor(contractPath).build());

        this.requestValidation = requestValidation;
        this.responseValidation = responseValidation;
    }

    @Bean
    public Filter validationFilter() {
        return new OpenApiValidationFilter(
                this.requestValidation, // enable request validation
                this.responseValidation  // enable response validation
        );
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(validationInterceptor);
    }
}
