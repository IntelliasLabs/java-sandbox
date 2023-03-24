package com.intellias.devsandbox;

import com.intellias.FolderGeneratorMojo;
import org.codehaus.plexus.util.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class FolderGeneratorMojoTest {

    private final FolderGeneratorMojo folderGeneratorMojo = new FolderGeneratorMojo();

    @Test
    public void createDirsTreeTest() throws IOException {
        // Given
        final String yamlPath = "src/test/resources/folder-tree.yaml";
        final String pathToSaveStructure = "src/test/resources/";
        final File rootDir = new File("src/test/resources/root");
        Assertions.assertFalse(rootDir.exists());

        // When
        folderGeneratorMojo.createDirsStructure(yamlPath, pathToSaveStructure);

        // Then
        Assertions.assertTrue(rootDir.exists());
        FileUtils.deleteDirectory(rootDir);
    }
}
