package com.writon.admin.domain.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @Id
  @Column(name = "user_id", columnDefinition = "int UNSIGNED not null")
  private Long id;

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

}