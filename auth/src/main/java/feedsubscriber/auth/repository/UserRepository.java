package feedsubscriber.auth.repository;

import feedsubscriber.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing users in the application.
 *
 * @author: ReLive
 * @date: 2022/8/02 20:20 下午
 */
@SuppressWarnings("JavadocDeclaration")
public interface UserRepository extends JpaRepository<User, Long> {
  User findUserByUsername(String username);
}
