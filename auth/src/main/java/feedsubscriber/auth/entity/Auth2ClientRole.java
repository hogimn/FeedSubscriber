package feedsubscriber.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents the association between OAuth2 clients and roles in the authentication system.
 *
 * @author: ReLive
 * @date: 2022/08/02 20:14 下午
 */
@SuppressWarnings({"JavadocDeclaration", "JpaDataSourceORMInspection"})
@Data
@Entity
@Table(name = "`oauth2_client_role`")
public class Auth2ClientRole {
  @Id
  private Long id;
  private String clientRegistrationId;
  private String roleCode;

  @ManyToOne
  @JoinTable(
      name = "oauth2_client_role_mapping",
      joinColumns = {
          @JoinColumn(name = "oauth_client_role_id")
      },
      inverseJoinColumns = {
          @JoinColumn(name = "role_id")
      }
  )
  private Role role;
}
