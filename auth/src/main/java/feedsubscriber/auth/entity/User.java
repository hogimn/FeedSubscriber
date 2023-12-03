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
 * Represents a user entity in the authentication system.
 *
 * @author: ReLive
 * @date: 2022/8/02 20:02 下午
 */
@SuppressWarnings({"JpaDataSourceORMInspection", "JavadocDeclaration"})
@Data
@Entity
@Table(name = "`user`")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  private String password;
  private String phone;
  private String email;

  @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_mtm_role",
      joinColumns = {
          @JoinColumn(name = "user_id")
      },
      inverseJoinColumns = {
          @JoinColumn(name = "role_id")
      }
  )
  private List<Role> roleList;
}
