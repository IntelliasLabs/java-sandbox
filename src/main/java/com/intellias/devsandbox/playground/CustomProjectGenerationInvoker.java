package com.intellias.devsandbox.playground;

import io.spring.initializr.generator.project.ProjectAssetGenerator;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.web.project.ProjectGenerationInvoker;
import io.spring.initializr.web.project.ProjectGenerationResult;
import io.spring.initializr.web.project.ProjectRequestToDescriptionConverter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;

import java.nio.file.Path;

public class CustomProjectGenerationInvoker extends ProjectGenerationInvoker<CustomProjectRequest> {

    public CustomProjectGenerationInvoker(ApplicationContext parentApplicationContext, ProjectRequestToDescriptionConverter<CustomProjectRequest> requestConverter) {
        super(parentApplicationContext, requestConverter);
    }

    protected CustomProjectGenerationInvoker(ApplicationContext parentApplicationContext, ApplicationEventPublisher eventPublisher, ProjectRequestToDescriptionConverter<CustomProjectRequest> requestConverter) {
        super(parentApplicationContext, eventPublisher, requestConverter);
    }

    @Override
    public ProjectGenerationResult invokeProjectStructureGeneration(CustomProjectRequest request) {
        return super.invokeProjectStructureGeneration(request);
    }

    @Override
    protected ProjectAssetGenerator<Path> getProjectAssetGenerator(ProjectDescription description) {
        return super.getProjectAssetGenerator(description);
    }

    @Override
    public byte[] invokeBuildGeneration(CustomProjectRequest request) {
        return super.invokeBuildGeneration(request);
    }

    @Override
    public Path createDistributionFile(Path dir, String extension) {
        return super.createDistributionFile(dir, extension);
    }

    @Override
    public void cleanTempFiles(Path dir) {
        super.cleanTempFiles(dir);
    }
}
