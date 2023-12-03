package feedsubscriber.restful.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configure security settings for the resource server, enabling OAuth2 resource server
 * functionality.
 *
 * @author: ReLive
 * @date: 2022/6/24 11:15 上午
 */
@SuppressWarnings({"JavadocDeclaration"})
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ResourceServerConfig {
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests((authorize) ->
            authorize
                .anyRequest()
                .authenticated())
        .oauth2ResourceServer()
        .jwt();
    return http.build();
  }

  /**
   * Creates a {@link JwtAuthenticationConverter} bean.
   *
   * @return The configured {@link JwtAuthenticationConverter}.
   */
  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    var grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
    grantedAuthoritiesConverter.setAuthorityPrefix("");

    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
    return jwtAuthenticationConverter;
  }
}
