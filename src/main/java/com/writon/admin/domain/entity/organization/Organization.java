package com.writon.admin.domain.entity.organization;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "organizations")
@Getter
@NoArgsConstructor
public class Organization {

  @Id
  @Column(name = "organization_id", columnDefinition = "int UNSIGNED not null")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false, length = 30)
  private String name;

  @Column(name = "logo")
  private String logo;

  @Column(name = "theme_color", nullable = false, length = 100)
  private String themeColor;

  @OneToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "admin_user_id")
  private AdminUser adminUser;

  public Organization(String name, String logo, String themeColor, AdminUser adminUser) {
    this.name = name;
    this.logo = logo;
    this.themeColor = themeColor;
    this.adminUser = adminUser;
  }
}