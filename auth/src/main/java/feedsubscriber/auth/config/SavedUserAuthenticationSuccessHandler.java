package feedsubscriber.auth.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Consumer;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * OAuth2 Login Success Handler.
 *
 * @author: ReLive
 * @date: 2022/8/4 19:59
 */
@SuppressWarnings({"JavadocDeclaration", "DataFlowIssue"})
public final class SavedUserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
  private final AuthenticationSuccessHandler delegate =
      new SavedRequestAwareAuthenticationSuccessHandler();

  private Consumer<OAuth2User> oauth2UserHandler = (user) -> {
  };

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication
  ) throws IOException, ServletException {
    if (authentication instanceof OAuth2AuthenticationToken) {
      if (authentication.getPrincipal() instanceof OAuth2User) {
        this.oauth2UserHandler.accept((OAuth2User) authentication.getPrincipal());
      }
    }
    this.delegate.onAuthenticationSuccess(request, response, authentication);
  }

  public void setOauth2UserHandler(Consumer<OAuth2User> oauth2UserHandler) {
    this.oauth2UserHandler = oauth2UserHandler;
  }
}
