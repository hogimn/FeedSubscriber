package feedsubscriber.auth.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;

/**
 * Represents a role entity in the authentication system.
 *
 * @author: ReLive
 * @date: 2022/8/02 20:10 下午
 */
@SuppressWarnings({"JpaDataSourceORMInspection", "JavadocDeclaration"})
@Data
@Entity
@Table(name = "`role`")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String roleCode;

  @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
  @JoinTable(
      name = "role_mtm_permission",
      joinColumns = {
          @JoinColumn(name = "role_id")
      },
      inverseJoinColumns = {
          @JoinColumn(name = "permission_id")
      }
  )
  private List<Permission> permissions;
}
