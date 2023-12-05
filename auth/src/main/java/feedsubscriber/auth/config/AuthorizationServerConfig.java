package feedsubscriber.auth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import feedsubscriber.auth.jose.Jwks;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * OAuth2 and OpenID Connect Configuration.
 *
 * @author ReLive
 * @date 2022/6/23 2:03 下午
 */
@SuppressWarnings("JavadocDeclaration")
@Configuration
public class AuthorizationServerConfig {
  /**
   * Configures the OAuth2 authorization server security filter chain.
   *
   * @param http The HttpSecurity instance to configure.
   * @return The configured SecurityFilterChain.
   * @throws Exception If an error occurs during configuration.
   */
  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
      throws Exception {
    var authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
    authorizationServerConfigurer.oidc(Customizer.withDefaults());

    RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

    return http
        .securityMatcher(endpointsMatcher)
        .authorizeHttpRequests(authorizeRequests ->
            authorizeRequests
                .anyRequest()
                .authenticated())
        .csrf(csrf ->
            csrf.ignoringRequestMatchers(endpointsMatcher))
        .apply(authorizationServerConfigurer)
        .and()
        .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
        .exceptionHandling(exceptions ->
            exceptions.authenticationEntryPoint(
                new LoginUrlAuthenticationEntryPoint("/login")))
        .apply(authorizationServerConfigurer)
        .and()
        .logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/login")
        .deleteCookies("JSESSIONID")
        .invalidateHttpSession(true)
        .and()
        .build();
  }

  /**
   * Persistent OAuth2 Client.
   *
   * @param jdbcTemplate The JdbcTemplate instance.
   * @return The RegisteredClientRepository bean.
   */
  @Bean
  public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
    RegisteredClient registeredClient1 = RegisteredClient
        .withId("relive-messaging-oidc")
        .clientId("relive-client")
        .clientSecret("{noop}relive-client")
        .clientAuthenticationMethods(s -> {
          s.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
          s.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
        })
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
        .redirectUri("http://localhost:9001/login/oauth2/code/messaging-gateway-oidc")
        .scope(OidcScopes.OPENID)
        .scope(OidcScopes.PROFILE)
        .scope(OidcScopes.EMAIL)
        .scope("read")
        .clientSettings(ClientSettings
            .builder()
            .requireAuthorizationConsent(false)
            .requireProofKey(false)
            .build())
        .tokenSettings(TokenSettings
            .builder()
            .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
            .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
            .accessTokenTimeToLive(Duration.ofSeconds(30 * 60))
            .refreshTokenTimeToLive(Duration.ofSeconds(60 * 60))
            .reuseRefreshTokens(true)
            .build())
        .build();

    RegisteredClient registeredClient2 = RegisteredClient
        .withId("react-messaging-oidc")
        .clientId("react-client")
        .clientSecret("{noop}react-client")
        .clientAuthenticationMethods(s -> {
          s.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
          s.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
        })
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
        .redirectUri("http://localhost:3000/authorized")
        .scope(OidcScopes.OPENID)
        .scope(OidcScopes.PROFILE)
        .scope(OidcScopes.EMAIL)
        .scope("read")
        .clientSettings(ClientSettings
            .builder()
            .requireAuthorizationConsent(false)
            .requireProofKey(false)
            .build())
        .tokenSettings(TokenSettings
            .builder()
            .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
            .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
            .accessTokenTimeToLive(Duration.ofSeconds(30 * 60))
            .refreshTokenTimeToLive(Duration.ofSeconds(60 * 60))
            .reuseRefreshTokens(true)
            .build())
        .build();

    var registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
    registeredClientRepository.save(registeredClient1);
    registeredClientRepository.save(registeredClient2);
    return registeredClientRepository;
  }

  /**
   * Responsible for persisting authorization information during the authorization process, such as
   * code, access_token, refresh_token.
   *
   * @param jdbcTemplate The JdbcTemplate instance.
   * @param registeredClientRepository The RegisteredClientRepository instance.
   * @return The OAuth2AuthorizationService bean.
   */
  @Bean
  public OAuth2AuthorizationService authorizationService(
      JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository
  ) {
    return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
  }

  /**
   * Provides a service for persisting user consent "authorization consent" information.
   *
   * @param jdbcTemplate The JdbcTemplate instance.
   * @param registeredClientRepository The RegisteredClientRepository instance.
   * @return The OAuth2AuthorizationConsentService bean.
   */
  @Bean
  public OAuth2AuthorizationConsentService authorizationConsentService(
      JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository
  ) {
    return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
  }

  /**
   * Provides settings for the authorization server.
   *
   * @return The AuthorizationServerSettings bean.
   */
  @Bean
  public AuthorizationServerSettings authorizationServerSettings() {
    return AuthorizationServerSettings
        .builder()
        .issuer("http://localhost:9004")
        .build();
  }

  /**
   * Provides a JSON Web Key (JWK) source for JWT decoding.
   *
   * @return The JWKSource bean.
   */
  @Bean
  public JWKSource<SecurityContext> jwkSource() {
    RSAKey rsaKey = Jwks.generateRsa();
    JWKSet jwkSet = new JWKSet(rsaKey);
    return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
  }

  @Bean
  public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
    return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
  }
}
