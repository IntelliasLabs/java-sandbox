package com.intellias.devsandbox.playground;

import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.web.controller.ProjectGenerationController;
import io.spring.initializr.web.project.ProjectGenerationInvoker;

import java.util.Map;


public class CustomProjectGenerationController extends ProjectGenerationController<CustomProjectRequest> {

    public CustomProjectGenerationController(InitializrMetadataProvider metadataProvider,
                                             ProjectGenerationInvoker<CustomProjectRequest> projectGenerationInvoker) {
        super(metadataProvider, projectGenerationInvoker);
    }

    @Override
    public CustomProjectRequest projectRequest(Map<String, String> headers) {
        return null;
    }
}
