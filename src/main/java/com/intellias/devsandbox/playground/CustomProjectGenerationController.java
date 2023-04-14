package com.intellias.devsandbox.playground;

import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.web.controller.ProjectGenerationController;
import io.spring.initializr.web.project.ProjectGenerationInvoker;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;


public class CustomProjectGenerationController extends ProjectGenerationController<CustomProjectRequest> {

    @Autowired
    private CustomProjectService customProjectService;

    private final ProjectGenerationInvoker customProjectGenerationInvoker;

    public CustomProjectGenerationController(InitializrMetadataProvider metadataProvider,
                                             ProjectGenerationInvoker<CustomProjectRequest> projectGenerationInvoker) {
        super(metadataProvider, projectGenerationInvoker);
        customProjectGenerationInvoker = projectGenerationInvoker;
    }

    @GetMapping(path = "createPoms")
    @SneakyThrows
    public ResponseEntity createPoms() {
        customProjectService.createPoms(customProjectGenerationInvoker);
        return ResponseEntity.ok().build();
    }

    @Override
    public CustomProjectRequest projectRequest(Map<String, String> headers) {
        CustomProjectRequest request = new CustomProjectRequest();
        request.getParameters().putAll(headers);
        request.initialize(getMetadata());
        return request;
    }
}
