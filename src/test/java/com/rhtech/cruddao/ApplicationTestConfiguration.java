package com.rhtech.cruddao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;


import javax.sql.DataSource;



/**
 * Created by rozaydin on 1/9/17.
 */
@Configuration
public class ApplicationTestConfiguration {

    @Bean("dataSource")
    public DataSource createDataSource() {
        BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName("org.h2.Driver");
        bds.setUrl("jdbc:h2:mem:ais");
        bds.setInitialSize(2);
        bds.setMaxTotal(2);
        return bds;
    }

    @Bean("JdbcTemplate")
    public JdbcTemplate createJdbcTemplate(@Autowired DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // http://stackoverflow.com/questions/16038360/initialize-database-without-xml-configuration-but-using-configuration
    @Bean
    public DataSourceInitializer dataSourceInitializer(@Autowired DataSource dataSource, @Autowired DatabasePopulator populator) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(populator);
        return initializer;
    }

    @Bean
    public DatabasePopulator databasePopulator(@Value("classpath:schema.sql") Resource schemaScript, @Value("classpath:data.sql") Resource dataScript) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaScript);
        // populator.addScript(dataScript);
        return populator;
    }


}
