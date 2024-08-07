package com.writon.admin.domain.entity.organization;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
public class Organization {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "organization_id", nullable = false)
  private Integer id;

  @Column(name = "name", nullable = false, length = 30)
  private String name;

  @Column(name = "logo")
  private String logo;

  @Column(name = "theme_color", nullable = false, length = 45)
  private String themeColor;

  @ColumnDefault("CURRENT_TIMESTAMP(6)")
  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @ColumnDefault("CURRENT_TIMESTAMP(6)")
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

}