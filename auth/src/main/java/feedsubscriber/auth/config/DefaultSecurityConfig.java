package feedsubscriber.auth.config;

import static org.springframework.security.config.Customizer.withDefaults;

import feedsubscriber.auth.repository.Auth2ClientRoleRepository;
import feedsubscriber.auth.repository.JdbcClientRegistrationRepository;
import feedsubscriber.auth.repository.UserRepository;
import feedsubscriber.auth.service.AuthorityMappingAuth2UserService;
import feedsubscriber.auth.service.JdbcUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Default Spring Web Security Configuration.
 *
 * @author: ReLive
 * @date: 2022/6/23 7:26 下午
 */
@SuppressWarnings({"JavadocDeclaration", "SpringJavaAutowiredFieldsWarningInspection"})
@Configuration
@EnableWebSecurity
public class DefaultSecurityConfig {
  @Autowired
  UserRepositoryAuth2UserHandler userHandler;

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authorizeRequests ->
            authorizeRequests
                .anyRequest()
                .authenticated())
        .formLogin(withDefaults())
        .oauth2Login(oauth2login -> {
          var successHandler = new SavedUserAuthenticationSuccessHandler();
          successHandler.setOauth2UserHandler(userHandler);
          oauth2login.successHandler(successHandler);
        })
        .logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("http://localhost:3000/")
        .deleteCookies("JSESSIONID")
        .invalidateHttpSession(true);
    return http.build();
  }

  /**
   * User information container class, used to obtain user information during Form authentication.
   *
   * @param userRepository The repository for accessing user data.
   * @return An instance of the UserDetailsService, specifically a JdbcUserDetailsService.
   */
  @Bean
  UserDetailsService userDetailsService(UserRepository userRepository) {
    return new JdbcUserDetailsService(userRepository);
  }

  /**
   * Extended OAuth2 login mapping permission information.
   *
   * @param auth2ClientRoleRepository The repository for OAuth2 client roles.
   * @return An instance of OAuth2UserService handling mapping of authorities.
   */
  @Bean
  OAuth2UserService<OAuth2UserRequest, OAuth2User> auth2UserService(
      Auth2ClientRoleRepository auth2ClientRoleRepository
  ) {
    return new AuthorityMappingAuth2UserService(auth2ClientRoleRepository);
  }

  /**
   * Persistent GitHub Client.
   *
   * @param jdbcTemplate The JDBC template for database access.
   * @return An instance of ClientRegistrationRepository for managing GitHub client registrations.
   */
  @Bean
  ClientRegistrationRepository clientRegistrationRepository(JdbcTemplate jdbcTemplate) {
    var jdbcClientRegistrationRepository = new JdbcClientRegistrationRepository(jdbcTemplate);
    // Please apply for the correct clientId and clientSecret on GitHub
    ClientRegistration clientRegistration = ClientRegistration
        .withRegistrationId("github")
        .clientId("123456")
        .clientSecret("123456")
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .redirectUri("{baseUrl}/{action}/oauth2/code/{registrationId}")
        .scope("read:user")
        .authorizationUri("https://github.com/login/oauth/authorize")
        .tokenUri("https://github.com/login/oauth/access_token")
        .userInfoUri("https://api.github.com/user")
        .userNameAttributeName("login")
        .clientName("GitHub")
        .build();

    jdbcClientRegistrationRepository.save(clientRegistration);
    return jdbcClientRegistrationRepository;
  }

  /**
   * Responsible for OAuth2AuthorizedClient persistence between web requests.
   *
   * @param jdbcTemplate                 The JDBC template for database access.
   * @param clientRegistrationRepository The repository for managing OAuth2 client registrations.
   * @return An instance of OAuth2AuthorizedClientService for persisting authorized clients.
   */
  @Bean
  OAuth2AuthorizedClientService authorizedClientService(
      JdbcTemplate jdbcTemplate,
      ClientRegistrationRepository clientRegistrationRepository) {
    return new JdbcOAuth2AuthorizedClientService(jdbcTemplate, clientRegistrationRepository);
  }

  /**
   * Used to save and persist authorized clients between requests.
   *
   * @param authorizedClientService The service responsible for managing OAuth2 authorized clients.
   * @return An instance of OAuth2AuthorizedClientRepository for saving and persisting authorized
   *         clients.
   */
  @Bean
  OAuth2AuthorizedClientRepository authorizedClientRepository(
      OAuth2AuthorizedClientService authorizedClientService) {
    return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService);
  }
}
