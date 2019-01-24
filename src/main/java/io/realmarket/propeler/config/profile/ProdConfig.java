package io.realmarket.propeler.config.profile;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application-production.yaml")
@Profile("production")
public class ProdConfig {}
