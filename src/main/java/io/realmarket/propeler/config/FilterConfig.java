package io.realmarket.propeler.config;

import io.realmarket.propeler.security.LoginFilter;
import io.realmarket.propeler.service.util.LoginIPAttemptsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

  @Autowired private LoginIPAttemptsService loginAttemptsService;

  @Bean
  public FilterRegistrationBean<LoginFilter> loginFilter() {
    FilterRegistrationBean<LoginFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new LoginFilter(loginAttemptsService));
    registrationBean.addUrlPatterns("/auth");
    return registrationBean;
  }
}
