package com.intellias.devsandbox.playground;

import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.web.project.ProjectGenerationInvoker;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public CustomProjectGenerationController projectGenerationController(InitializrMetadataProvider metadataProvider,
                                                                         ApplicationContext applicationContext) {
        CustomProjectGenerationInvoker projectGenerationInvoker = new CustomProjectGenerationInvoker(
                applicationContext, new CustomProjectRequestToDescriptionConverter());
        return new CustomProjectGenerationController(metadataProvider, projectGenerationInvoker);
    }
}
