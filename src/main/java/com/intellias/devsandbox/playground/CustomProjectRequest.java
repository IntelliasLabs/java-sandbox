package com.intellias.devsandbox.playground;

import io.spring.initializr.web.project.WebProjectRequest;

public class CustomProjectRequest extends WebProjectRequest {

    private String testAttribute;

    public String getTestAttribute() {
        return testAttribute;
    }

    public void setTestAttribute(String testAttribute) {
        this.testAttribute = testAttribute;
    }
}
