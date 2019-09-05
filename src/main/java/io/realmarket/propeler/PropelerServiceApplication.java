package io.realmarket.propeler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@SpringBootApplication
@EnableAsync
@EnableJpaAuditing
public class PropelerServiceApplication {

  public static final String PLATFORM_NAME = "";

  public static void main(String[] args) {
    SpringApplication.run(PropelerServiceApplication.class, args);
  }

  @Value("${platform.name}")
  private void setPlatformName(String platformName)
      throws NoSuchFieldException, IllegalAccessException {
    Field platformNameField = PropelerServiceApplication.class.getDeclaredField("PLATFORM_NAME");
    platformNameField.setAccessible(true);

    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(platformNameField, platformNameField.getModifiers() & ~Modifier.FINAL);

    platformNameField.set(null, platformName);
  }
}
