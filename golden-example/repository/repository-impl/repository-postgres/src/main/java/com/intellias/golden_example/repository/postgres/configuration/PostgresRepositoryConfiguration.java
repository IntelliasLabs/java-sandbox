package com.intellias.golden_example.repository.postgres.configuration;

import com.intellias.golden_example.repository.postgres.BooksRepository;
import org.jooq.DSLContext;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(value = "repository.postgres.enabled", havingValue = "true")
public class PostgresRepositoryConfiguration {

    @Bean
    public DefaultDSLContext dsl(DataSourceConnectionProvider dataSourceConnectionProvider) {
        return new DefaultDSLContext(configuration(dataSourceConnectionProvider));
    }

    @Bean
    public DataSourceConnectionProvider connectionProvider(DataSource dataSource) {
        TransactionAwareDataSourceProxy dataSourceProxy = new TransactionAwareDataSourceProxy(dataSource);
        return new DataSourceConnectionProvider(dataSourceProxy);
    }

    @Bean
    public BooksRepository booksRepository(DSLContext dslContext) {
        return new BooksRepository(dslContext);
    }

    private DefaultConfiguration configuration(DataSourceConnectionProvider dataSourceConnectionProvider) {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.set(dataSourceConnectionProvider);
        return jooqConfiguration;
    }

}
