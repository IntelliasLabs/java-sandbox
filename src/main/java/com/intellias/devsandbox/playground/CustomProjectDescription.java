package com.intellias.devsandbox.playground;

import io.spring.initializr.generator.project.ProjectDescription;

public interface CustomProjectDescription extends ProjectDescription {

    String getTestAttribute();
}
