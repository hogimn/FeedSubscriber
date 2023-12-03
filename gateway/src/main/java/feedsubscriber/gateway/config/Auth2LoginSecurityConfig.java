package feedsubscriber.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Security configuration for OAuth2 login.
 */
@Configuration
@EnableWebFluxSecurity
public class Auth2LoginSecurityConfig {
  /**
   * Configures security settings for OAuth2 login.
   *
   * @param http The {@link ServerHttpSecurity} to configure.
   * @return The configured {@link SecurityWebFilterChain}.
   */
  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    http.authorizeExchange()
        .anyExchange().permitAll()
        .and()
        .oauth2Login()
        .and()
        .httpBasic().disable()
        .formLogin().disable()
        .csrf().disable();

    return http.build();
  }
}
