package feedsubscriber.auth.repository;

import feedsubscriber.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing roles in the application.
 *
 * @author: ReLive
 * @date: 2022/8/4 12:28
 */
@SuppressWarnings("JavadocDeclaration")
public interface RoleRepository extends JpaRepository<Role, Long> {
  Role findByRoleCode(String roleCode);
}
