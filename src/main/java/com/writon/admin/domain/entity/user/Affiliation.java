package com.writon.admin.domain.entity.user;

import com.writon.admin.domain.entity.organization.Organization;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "affiliations")
public class Affiliation {

  @Id
  @Column(name = "affiliation_id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Column(name = "hire_date")
  private LocalDate hireDate;

  @Column(name = "position", length = 20)
  private String position;

  @Column(name = "position_intorduce", length = 300)
  private String positionIntorduce;

  @Column(name = "nickname", nullable = false, length = 40)
  private String nickname;

  @Column(name = "company", length = 50)
  private String company;

  @Column(name = "company_public", nullable = false)
  private Boolean companyPublic = false;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "organization_id", nullable = false)
  private Organization organization;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

}