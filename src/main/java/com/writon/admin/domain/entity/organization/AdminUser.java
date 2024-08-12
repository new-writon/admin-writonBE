package com.writon.admin.domain.entity.organization;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "admin_user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUser {

  @Id
  @Column(name = "admin_user_id", columnDefinition = "int UNSIGNED not null")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "identifier", nullable = false)
  private String identifier;

  @Column(name = "password", nullable = false)
  private String password;

  public AdminUser(String identifier, String password) {
    this.identifier = identifier;
    this.password = password;
  }
}