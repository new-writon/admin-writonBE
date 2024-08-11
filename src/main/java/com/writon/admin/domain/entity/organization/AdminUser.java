package com.writon.admin.domain.entity.organization;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "admin_user")
public class AdminUser {

  @Id
  @Column(name = "admin_user_id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Column(name = "identifier", nullable = false)
  private String identifier;

  @Column(name = "password", nullable = false)
  private String password;

}