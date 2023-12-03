package feedsubscriber.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents a permission entity in the authentication system.
 *
 * @author: ReLive
 * @date: 2022/8/4 12:22
 */
@SuppressWarnings({"JavadocDeclaration", "JpaDataSourceORMInspection"})
@Data
@Entity
@Table(name = "`permission`")
public class Permission {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String permissionName;
  private String permissionCode;
}
