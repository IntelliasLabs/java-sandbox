package com.intellias.devsandbox.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
@Service
public class CreateDirsTreeService {

    private final String pathToSaveStructure = "src/main/resources/";

    public void createDirsStructure(String yamlPath) throws FileNotFoundException {

        InputStream inputStream = new FileInputStream(yamlPath);
        Map<String, ArrayList<Object>> data = new Yaml().load(inputStream);
        log.info("Yaml folders structure: " + data);

        createDirsforNode(data, pathToSaveStructure);
    }

    private void createDirsforNode(Map<String, ArrayList<Object>> data, String mainPath) {
        data.forEach((key, value) -> {
            makeDir(mainPath + "/" + key);
            value.forEach(obj -> {
                if (obj instanceof String) {
                    makeDir(mainPath + "/" + key + "/" + obj);
                } else if (obj instanceof Map<?, ?>) {
                    Map<String, ArrayList<Object>> map = (Map<String, ArrayList<Object>>) obj;
                    createDirsforNode(map, mainPath + "/" + key);
                }
            });
        });
    }

    private boolean makeDir(String path) {
        boolean bool = new File(path).mkdirs();
        if (bool) {
            log.debug("Directory created successfully with path: " + path);
        } else {
            log.debug("Sorry couldn't create specified directory with path: " + path);
        }
        return bool;
    }
}