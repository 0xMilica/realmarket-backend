package io.realmarket.propeler.config.db;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfiguration {

  private DataSource dataSource;

  @Autowired
  public LiquibaseConfiguration(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Bean
  @DependsOn(value = {"dataSource"})
  public SpringLiquibase liquibase() {

    SpringLiquibase liquibase = new SpringLiquibase();
    liquibase.setChangeLog("classpath:dbMigrations/master.xml");
    liquibase.setDataSource(dataSource);

    return liquibase;
  }
}
