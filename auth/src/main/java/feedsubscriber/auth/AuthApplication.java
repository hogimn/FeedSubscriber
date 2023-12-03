package feedsubscriber.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class to bootstrap the authentication application.
 *
 * @author: ReLive
 * @date: 2022/6/23 2:02 下午
 */
@SuppressWarnings("JavadocDeclaration")
@SpringBootApplication
public class AuthApplication {
  public static void main(String[] args) {
    SpringApplication.run(AuthApplication.class, args);
  }
}
