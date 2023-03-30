package com.intellias.devsandbox.playground;

import io.spring.initializr.web.project.ProjectRequest;

public class CustomProjectRequest extends ProjectRequest {

    private String testAttribute;

    public String getTestAttribute() {
        return testAttribute;
    }

    public void setTestAttribute(String testAttribute) {
        this.testAttribute = testAttribute;
    }
}
