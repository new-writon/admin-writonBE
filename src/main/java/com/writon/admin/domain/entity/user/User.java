package com.writon.admin.domain.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
public class User {

  @Id
  @Column(name = "user_id", nullable = false)
  private Integer id;

  @Column(name = "role", nullable = false, length = 20)
  private String role;

  @Column(name = "identifier", nullable = false, length = 40)
  private String identifier;

  @Column(name = "password")
  private String password;

  @Column(name = "email", nullable = false, length = 40)
  private String email;

  @Column(name = "profile", length = 500)
  private String profile;

  @Column(name = "account_number", length = 40)
  private String accountNumber;

  @Column(name = "bank", length = 20)
  private String bank;

  @ColumnDefault("CURRENT_TIMESTAMP(6)")
  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @ColumnDefault("CURRENT_TIMESTAMP(6)")
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

}