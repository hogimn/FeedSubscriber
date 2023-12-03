package feedsubscriber.auth.service;

import feedsubscriber.auth.entity.Auth2ClientRole;
import feedsubscriber.auth.entity.Role;
import feedsubscriber.auth.repository.Auth2ClientRoleRepository;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.CollectionUtils;

/**
 * Permission mapping service, OAuth2 login users will be given corresponding permissions,
 * if the mapping permission is empty, the lowest permission ROLE_OPERATION will be given.
 *
 * <p>
 * Note: `authority` and `role` are custom permission information fields in this example,
 * which are not specified in the OAuth2 protocol and the OpenID Connect protocol
 * </p>
 *
 * @author: ReLive
 * @date: 2022/7/12 6:31 下午
 */
@SuppressWarnings({"JavadocDeclaration", "unchecked"})
@RequiredArgsConstructor
public class AuthorityMappingAuth2UserService
    implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
  private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
  private final Auth2ClientRoleRepository auth2ClientRoleRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    Map<String, Object> additionalParameters = userRequest.getAdditionalParameters();
    Set<String> role = new HashSet<>();
    if (additionalParameters.containsKey("authority")) {
      role.addAll((Collection<? extends String>) additionalParameters.get("authority"));
    }
    if (additionalParameters.containsKey("role")) {
      role.addAll((Collection<? extends String>) additionalParameters.get("role"));
    }
    Set<SimpleGrantedAuthority> mappedAuthorities = role
        .stream()
        .map(r -> auth2ClientRoleRepository
            .findByClientRegistrationIdAndRoleCode(
                userRequest
                    .getClientRegistration()
                    .getRegistrationId(),
                r))
        .map(Auth2ClientRole::getRole)
        .map(Role::getRoleCode)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toSet());

    if (CollectionUtils.isEmpty(mappedAuthorities)) {
      mappedAuthorities = new HashSet<>(
          Collections.singletonList(
              new SimpleGrantedAuthority("ROLE_OPERATION")));
    }

    String userNameAttributeName = userRequest
        .getClientRegistration()
        .getProviderDetails()
        .getUserInfoEndpoint()
        .getUserNameAttributeName();

    DefaultOAuth2User auth2User = (DefaultOAuth2User) delegate.loadUser(userRequest);

    return new DefaultOAuth2User(
        mappedAuthorities,
        auth2User.getAttributes(),
        userNameAttributeName);
  }
}
