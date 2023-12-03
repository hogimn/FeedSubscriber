package feedsubscriber.auth.repository;

import feedsubscriber.auth.entity.Auth2ClientRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing OAuth2 client roles associated with client registrations.
 *
 * @author: ReLive
 * @date: 2022/8/02 20:30 下午
 */
@SuppressWarnings("JavadocDeclaration")
public interface Auth2ClientRoleRepository extends JpaRepository<Auth2ClientRole, Long> {
  Auth2ClientRole findByClientRegistrationIdAndRoleCode(
      String clientRegistrationId, String roleCode);
}
