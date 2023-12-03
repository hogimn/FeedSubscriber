package feedsubscriber.auth.config;

import feedsubscriber.auth.entity.Permission;
import feedsubscriber.auth.repository.RoleRepository;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * Custom Access Token
 *
 * <p>This example uses the RBAC0 permission model to query the corresponding permissions of the
 * role based on the obtained role information in the security context, add the permission to the
 * access token, and replace the original value in the scope.
 *
 * @author: ReLive
 * @date: 2022/8/7 20:22
 */
@SuppressWarnings({"JavadocDeclaration", "SpringJavaAutowiredFieldsWarningInspection"})
@Configuration
public class AccessTokenCustomizerConfig {
  @Autowired
  RoleRepository roleRepository;

  /**
   * Configures a custom OAuth2 token customizer.
   *
   * <p>This customizer adds RBAC permissions to the access token based on the user's role
   * information obtained from the security context.
   *
   * @return The configured OAuth2 token customizer.
   */
  @Bean
  public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
    return (context) -> {
      if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
        context
            .getClaims()
            .claims(claim ->
                claim.put("authorities",
                    roleRepository
                        .findByRoleCode(context
                            .getPrincipal()
                            .getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .findFirst()
                            .orElse("ROLE_OPERATION"))
                        .getPermissions()
                        .stream()
                        .map(Permission::getPermissionCode)
                        .collect(Collectors.toSet())));
      }
    };
  }
}
