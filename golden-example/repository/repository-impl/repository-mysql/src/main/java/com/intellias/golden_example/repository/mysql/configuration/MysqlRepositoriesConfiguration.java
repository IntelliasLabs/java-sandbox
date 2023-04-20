package com.intellias.golden_example.repository.mysql.configuration;

import com.intellias.golden_example.repository.IBooksRepository;
import com.intellias.golden_example.repository.mysql.BooksJPARepository;
import com.intellias.golden_example.repository.mysql.BooksRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "repository.mysql.enabled", havingValue = "true", matchIfMissing = true)
public class MysqlRepositoriesConfiguration {

    @Bean
    @ConditionalOnMissingBean(ModelMapper.class)
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public IBooksRepository simpleEntityRepository(BooksJPARepository booksJPARepository,
                                                   ModelMapper modelMapper) {
        return new BooksRepository(
                booksJPARepository,
                modelMapper
        );
    }

}
