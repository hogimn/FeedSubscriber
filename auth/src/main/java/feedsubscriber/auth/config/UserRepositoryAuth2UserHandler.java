package feedsubscriber.auth.config;

import feedsubscriber.auth.entity.Role;
import feedsubscriber.auth.entity.User;
import feedsubscriber.auth.repository.RoleRepository;
import feedsubscriber.auth.repository.UserRepository;
import java.util.Collections;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

/**
 * Persist OAuth2 Login User to User Information Table.
 *
 * @author: ReLive
 * @date: 2022/8/4 19:51
 */
@SuppressWarnings("JavadocDeclaration")
@Component
@RequiredArgsConstructor
public final class UserRepositoryAuth2UserHandler implements Consumer<OAuth2User> {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  @Override
  public void accept(OAuth2User auth2User) {
    DefaultOAuth2User defaultAuth2User = (DefaultOAuth2User) auth2User;
    if (this.userRepository.findUserByUsername(auth2User.getName()) == null) {
      User user = new User();
      user.setUsername(defaultAuth2User.getName());
      Role role = roleRepository
          .findByRoleCode(defaultAuth2User
              .getAuthorities()
              .stream()
              .map(GrantedAuthority::getAuthority)
              .findFirst()
              .orElse("ROLE_OPERATION"));
      user.setRoleList(Collections.singletonList(role));
      userRepository.save(user);
    }
  }
}
