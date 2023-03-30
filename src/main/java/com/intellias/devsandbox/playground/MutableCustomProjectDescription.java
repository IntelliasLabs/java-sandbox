package com.intellias.devsandbox.playground;

import io.spring.initializr.generator.buildsystem.BuildSystem;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.language.Language;
import io.spring.initializr.generator.packaging.Packaging;
import io.spring.initializr.generator.version.Version;

import java.util.LinkedHashMap;
import java.util.Map;

public class MutableCustomProjectDescription implements CustomProjectDescription {

    private Version platformVersion;

    private BuildSystem buildSystem;

    private Packaging packaging;

    private Language language;

    private final Map<String, Dependency> requestedDependencies = new LinkedHashMap<>();

    private String groupId;

    private String artifactId;

    private String version;

    private String name;

    private String description;

    private String applicationName;

    private String packageName;

    private String baseDirectory;

    private String testAttribute;

    @Override
    public Version getPlatformVersion() {
        return platformVersion;
    }

    public void setPlatformVersion(Version platformVersion) {
        this.platformVersion = platformVersion;
    }

    @Override
    public BuildSystem getBuildSystem() {
        return buildSystem;
    }

    public void setBuildSystem(BuildSystem buildSystem) {
        this.buildSystem = buildSystem;
    }

    @Override
    public Packaging getPackaging() {
        return packaging;
    }

    public void setPackaging(Packaging packaging) {
        this.packaging = packaging;
    }

    @Override
    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public Map<String, Dependency> getRequestedDependencies() {
        return requestedDependencies;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    @Override
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String getBaseDirectory() {
        return baseDirectory;
    }

    public void setBaseDirectory(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    @Override
    public String getTestAttribute() {
        return testAttribute;
    }

    public void setTestAttribute(String testAttribute) {
        this.testAttribute = testAttribute;
    }

    public Dependency addDependency(String id, Dependency dependency) {
        return this.requestedDependencies.put(id, dependency);
    }
}
