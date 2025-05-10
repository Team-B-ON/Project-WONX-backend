package io.github.bon.wonx.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  // 이렇게 빈으로 등록하면 서비스 클래스에서 바로 주입 받아 사용 가능
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
