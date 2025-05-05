package com.vinzlac.kata.kafkapgspringbootjava.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.dialect.H2Dialect;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@TestConfiguration
public class H2TestConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.vinzlac.kata.kafkapgspringbootjava");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", H2Dialect.class.getName());
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty("hibernate.show_sql", "true");
        // Désactiver les commandes PostgreSQL
        properties.setProperty("hibernate.hql.bulk_id_strategy", "org.hibernate.hql.spi.id.inline.InlineIdsOrClauseBulkIdStrategy");
        
        em.setJpaProperties(properties);
        return em;
    }
} 