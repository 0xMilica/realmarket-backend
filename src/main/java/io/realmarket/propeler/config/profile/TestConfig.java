package io.realmarket.propeler.config.profile;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Configuration
@PropertySource("classpath:application-testing.yaml")
@Profile("testing")
public class TestConfig {}
