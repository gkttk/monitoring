package com.gkttk.monitoring.components;

import io.micrometer.common.util.StringUtils;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SchemaInitializer implements BeanPostProcessor {

  @Value("${spring.liquibase.default-schema}")
  private String schemaName;

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if (!StringUtils.isEmpty(schemaName) && bean instanceof DataSource) {
      DataSource dataSource = (DataSource) bean;
      try (Connection conn = dataSource.getConnection();
          Statement statement = conn.createStatement()) {
        log.info("Creating DB schema '{}' if not exists.", schemaName);
        statement.execute("CREATE SCHEMA IF NOT EXISTS " + schemaName);
      } catch (SQLException e) {
        throw new RuntimeException("Failed to create schema '" + schemaName + "'", e);
      }
    }
    return bean;
  }
}
