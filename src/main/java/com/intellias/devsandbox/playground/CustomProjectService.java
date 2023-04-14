package com.intellias.devsandbox.playground;

import io.spring.initializr.web.project.ProjectGenerationInvoker;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomProjectService {

    private final PomStructureProperties pomStructureProperties;
    private final String pathToRootDir = "src/main/resources/root";

    public void createPoms(ProjectGenerationInvoker customProjectGenerationInvoker) {
        if (!new File(pathToRootDir).exists()) {
            log.info("Root dir doesn't exist");
            return;
        }

        byte[] mavenRootPom = customProjectGenerationInvoker.invokeBuildGeneration(pomStructureProperties.getSpecification());
        savePom(pathToRootDir, mavenRootPom);

        for (Map<String, CustomProjectRequest> module : pomStructureProperties.getModules()) {
            for (Map.Entry<String, CustomProjectRequest> entry : module.entrySet()) {
                byte[] mavenPom = customProjectGenerationInvoker.invokeBuildGeneration(entry.getValue());
                savePom(pathToRootDir + "/" + entry.getKey(), mavenPom);
            }
        }
    }

    @SneakyThrows
    private void savePom(String pathName, byte[] data) {
        try (FileOutputStream fos = new FileOutputStream(pathName + "/pom.xml")) {
            fos.write(data);
            log.info("Pom successfully saved to {}", pathName);
        }
    }
}
