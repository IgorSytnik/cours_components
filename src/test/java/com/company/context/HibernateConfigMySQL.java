package com.company.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@Profile("mysql")
public class HibernateConfigMySQL {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/hei_schema");
        dataSource.setUsername("root");
        dataSource.setPassword("<L>(m){a}[0]");

        return dataSource;
    }


    /*
        create – Hibernate first drops existing tables, then creates new tables
        update – the object model created based on the mappings (annotations or XML) is compared with the existing schema, and then Hibernate updates the schema according to the diff. It never deletes the existing tables or columns even if they are no more required by the application
        create-drop – similar to create, with the addition that Hibernate will drop the database after all operations are completed. Typically used for unit testing
        validate – Hibernate only validates whether the tables and columns exist, otherwise it throws an exception
        none – this value effectively turns off the DDL generation
         */
//    private Properties hibernateProperties() {
//        Properties hibernateProperties = new Properties();
//        hibernateProperties.setProperty(
//                "hibernate.hbm2ddl.auto", "create");
//        hibernateProperties.setProperty(
//                "hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
//
//        hibernateProperties.setProperty("hibernate.format_sql", "true");
//        hibernateProperties.setProperty("hibernate.show_sql", "true");
//        return hibernateProperties;
//    }
}