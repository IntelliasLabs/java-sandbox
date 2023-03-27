package com.intellias.devsandbox.playground;

import io.spring.initializr.generator.buildsystem.BuildSystem;
import io.spring.initializr.generator.language.Language;
import io.spring.initializr.generator.packaging.Packaging;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.Type;
import io.spring.initializr.metadata.support.MetadataBuildItemMapper;
import io.spring.initializr.web.project.InvalidProjectRequestException;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.initializr.web.project.ProjectRequestToDescriptionConverter;

import java.util.List;
import java.util.stream.Collectors;

public class CustomProjectRequestToDescriptionConverter implements ProjectRequestToDescriptionConverter<CustomProjectRequest> {

    @Override
    public CustomProjectDescription convert(CustomProjectRequest request, InitializrMetadata metadata) {
        MutableCustomProjectDescription description = new MutableCustomProjectDescription();

        Version platformVersion = Version.parse(request.getBootVersion());
        List<Dependency> resolvedDependencies = getResolvedDependencies(request, platformVersion, metadata);
        validateDependencyRange(platformVersion, resolvedDependencies);

        description.setApplicationName(request.getApplicationName());
        description.setArtifactId(request.getArtifactId());
        description.setBaseDirectory(request.getBaseDir());
        description.setBuildSystem(getBuildSystem(request, metadata));
        description.setDescription(request.getDescription());
        description.setGroupId(request.getGroupId());
        description.setLanguage(Language.forId(request.getLanguage(), request.getJavaVersion()));
        description.setName(request.getName());
        description.setPackageName(request.getPackageName());
        description.setPackaging(Packaging.forId(request.getPackaging()));
        description.setPlatformVersion(platformVersion);
        description.setVersion(request.getVersion());
        resolvedDependencies.forEach((dependency) -> description.addDependency(dependency.getId(),
                MetadataBuildItemMapper.toDependency(dependency)));

        description.setTestAttribute(request.getTestAttribute());

        return description;
    }

    private BuildSystem getBuildSystem(ProjectRequest request, InitializrMetadata metadata) {
        Type typeFromMetadata = metadata.getTypes().get(request.getType());
        return BuildSystem.forId(typeFromMetadata.getTags().get("build"));
    }

    private List<Dependency> getResolvedDependencies(ProjectRequest request, Version platformVersion,
                                                     InitializrMetadata metadata) {
        List<String> depIds = request.getDependencies();
        return depIds.stream().map((it) -> {
            Dependency dependency = metadata.getDependencies().get(it);
            return dependency.resolve(platformVersion);
        }).collect(Collectors.toList());
    }

    private void validateDependencyRange(Version platformVersion, List<Dependency> resolvedDependencies) {
        resolvedDependencies.forEach((dep) -> {
            if (!dep.match(platformVersion)) {
                throw new InvalidProjectRequestException(
                        "Dependency '" + dep.getId() + "' is not compatible " + "with Spring Boot " + platformVersion);
            }
        });
    }
}
