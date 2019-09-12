package io.realmarket.propeler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableJpaAuditing
public class PropelerServiceApplication {

  public static final String PLATFORM_NAME = "Realmarket";

  public static void main(String[] args) {
    SpringApplication.run(PropelerServiceApplication.class, args);
  }
}
