package com.intellias;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Mojo(name = "folder-generator", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class FolderGeneratorMojo extends AbstractMojo {

    private static final String INTELLIAS_PLUGIN_STARTED_EXECUTING = "Intellias Plugin folder-generator started executing %s";
    private static final String INTELLIAS_PLUGIN_EXCEPTION = "Intellias Plugin folder-generator exception: {}";
    private static final String YAML_FOLDER_STRUCTURE = "Yaml folders structure: ";
    private static final String DIRECTORY_CREATED = "Directory created successfully with path: %s";
    private static final String COULD_NOT_CREATE_DIRECTORY = "Sorry couldn't create specified directory with path: %s";
    private static final String YAML_FILE_EMPTY = "Yaml file %s is empty, skip generation";

    @Parameter(defaultValue = "src/main/resources/folder-structure.yaml", property = "src/main/resources/folder-structure.yaml", required = true)
    private String pathToYamlFile;
    @Parameter(defaultValue = "src/main/resources/", property = "pathToSaveStructure", required = true)
    private String pathToSaveStructure;

    public void execute() {
        try {
            getLog().info(String.format(INTELLIAS_PLUGIN_STARTED_EXECUTING, new Date()));
            createDirsStructure(pathToYamlFile, pathToSaveStructure);
        } catch (Exception e) {
            getLog().error(INTELLIAS_PLUGIN_EXCEPTION, e);
        }
    }

    public void createDirsStructure(String yamlPath, String pathToSaveStructure) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(yamlPath);
        Map<String, ArrayList<Object>> data = new Yaml().load(inputStream);
        getLog().info(YAML_FOLDER_STRUCTURE + data);
        if (data == null || data.size() == 0) {
            getLog().warn(String.format(YAML_FILE_EMPTY, yamlPath));
            return;
        }
        createDirsForNode(data, pathToSaveStructure);
    }

    private void createDirsForNode(Map<String, ArrayList<Object>> data, String mainPath) {
        data.forEach((key, value) -> {
            makeDir(mainPath + "/" + key);
            value.forEach(node -> {
                if (node instanceof String) {
                    makeDir(mainPath + "/" + key + "/" + node);
                } else if (node instanceof Map<?, ?>) {
                    Map<String, ArrayList<Object>> map = (Map<String, ArrayList<Object>>) node;
                    createDirsForNode(map, mainPath + "/" + key);
                }
            });
        });
    }

    private boolean makeDir(String path) {
        boolean bool = new File(path).mkdirs();
        if (bool) {
            getLog().debug(String.format(DIRECTORY_CREATED, path));
        } else {
            getLog().debug(String.format(COULD_NOT_CREATE_DIRECTORY, path));
        }
        return bool;
    }
}
