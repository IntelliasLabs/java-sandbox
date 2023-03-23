package com.intellias.devsandbox.service;


import lombok.SneakyThrows;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class CreateDirsTreeServiceTest {

    private final CreateDirsTreeService createDirsTreeService = new CreateDirsTreeService();

    @SneakyThrows
    @Test
    public void createDirsTreeTest() {
        final String yamlPath = "src/test/resources/folder-tree.yaml";
        final File rootDir = new File("src/main/resources/root");

        Assertions.assertFalse(rootDir.exists());

        createDirsTreeService.createDirsStructure(yamlPath);

        Assertions.assertTrue(rootDir.exists());

        FileUtils.deleteDirectory(rootDir);
    }
}
