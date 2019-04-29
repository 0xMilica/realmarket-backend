package io.realmarket.propeler.config.security;

import io.realmarket.propeler.security.AuthorizationFilter;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private static final int STRENGTH = 10;

  private final JWTService jwtService;
  private final AuthService authService;

  @Autowired
  WebSecurityConfig(JWTService jwtService, @Lazy AuthService authService) {
    this.jwtService = jwtService;
    this.authService = authService;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(STRENGTH);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors()
        .and()
        .csrf()
        .disable()
        .authorizeRequests()
        .anyRequest()
        .authenticated()
        .and()
        .addFilterAt(
            new AuthorizationFilter(jwtService, authService), BasicAuthenticationFilter.class)
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring()
        .antMatchers("/swagger**", "/webjars/**", "/swagger-resources/**", "/v2/api-docs")
        .antMatchers("/csrf")
        .antMatchers("/")
        .antMatchers("/error")
        .antMatchers(HttpMethod.HEAD, "/users/**")
        .antMatchers(HttpMethod.PATCH, "/auth/reset_password")
        .antMatchers(HttpMethod.GET, "/files/public/**")
        .antMatchers(
            HttpMethod.POST,
            "/auth",
            "/auth/register",
            "/auth/reset_password",
            "/auth/recover_username",
            "/auth/confirm_registration",
            "/auth/recover_username",
            "/auth/2fa",
            "/auth/2fa/remember_me",
            "/auth/2fa/secret",
            "/auth/2fa/verify")
        .antMatchers(HttpMethod.PATCH, "/auth/email_confirm");
    /*
    .antMatchers(HttpMethod.POST, "**")
    .antMatchers(HttpMethod.PATCH, "**")
    .antMatchers(HttpMethod.GET, "**");
     //left these commented lines on purpose,
     //because it is easier to just uncomment them,
     //then retyping them again.*/
  }

  @Bean
  CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.addExposedHeader("Authorization");
    config.addExposedHeader("Set-Cookie");
    config.setAllowCredentials(true);
    config.addAllowedOrigin("*");
    config.addAllowedMethod("*");
    config.addAllowedHeader("*");

    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }
}
