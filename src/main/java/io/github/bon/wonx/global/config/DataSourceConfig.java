package io.github.bon.wonx.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {
  @Bean
  public DataSource dataSource() {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(DotenvConfig.get("DB_URL"));
    dataSource.setUsername(DotenvConfig.get("DB_USERNAME"));
    dataSource.setPassword(DotenvConfig.get("DB_PASSWORD"));
    return dataSource;
  }
}
