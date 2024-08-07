package com.writon.admin.domain.entity.user;

import com.writon.admin.domain.entity.organization.Organization;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import java.time.Instant;
import java.time.LocalDate;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
public class Affiliation {

  @EmbeddedId
  private AffiliationId id;

  @MapsId("organizationId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "organization_id", nullable = false)
  private Organization organization;

  @MapsId("userId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "hire_date")
  private LocalDate hireDate;

  @Column(name = "position", length = 20)
  private String position;

  @Column(name = "position_introduce", length = 300)
  private String positionIntroduce;

  @Column(name = "nickname", nullable = false, length = 191)
  private String nickname;

  @Column(name = "company", length = 50)
  private String company;

  @Column(name = "company_public")
  private Boolean companyPublic;

  @ColumnDefault("CURRENT_TIMESTAMP(6)")
  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @ColumnDefault("CURRENT_TIMESTAMP(6)")
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

}