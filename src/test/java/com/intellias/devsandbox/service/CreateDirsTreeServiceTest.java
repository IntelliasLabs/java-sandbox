package com.intellias.devsandbox.service;


import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class CreateDirsTreeServiceTest {

    private final CreateDirsTreeService createDirsTreeService = new CreateDirsTreeService();

    @SneakyThrows
    @Test
    public void createDirsTreeTest() {
        final String yamlPath = "src/test/resources/folder-tree.yaml";

        createDirsTreeService.createDirsStructure(yamlPath);
    }
}
