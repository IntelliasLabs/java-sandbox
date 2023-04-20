package com.intellias.devsandbox.playground;

import lombok.Data;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.util.List;
import java.util.Map;
import java.util.Properties;

@PropertySource(value = "classpath:poms-structure.yaml", factory = YamlPropertySourceFactory.class)
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "root")
@Data
public class PomStructureProperties {

    private CustomProjectRequest specification;
    private List<Map<String, CustomProjectRequest>> modules;

}

class YamlPropertySourceFactory implements PropertySourceFactory {

    @Override
    public org.springframework.core.env.PropertySource<?> createPropertySource(String name, EncodedResource encodedResource) {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(encodedResource.getResource());

        Properties properties = factory.getObject();

        return new PropertiesPropertySource(encodedResource.getResource().getFilename(), properties);
    }
}
