package com.writon.admin.domain.entity.challenge;

import com.writon.admin.domain.entity.organization.Organization;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "challenges")
@NoArgsConstructor
public class Challenge {

  @Id
  @Column(name = "challenge_id", columnDefinition = "int UNSIGNED not null")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false, length = 40)
  private String name;

  @Column(name = "start_at", nullable = false)
  private LocalDate startAt;

  @Column(name = "finish_at", nullable = false)
  private LocalDate finishAt;

  @Column(name = "deposit", columnDefinition = "int UNSIGNED")
  private Long deposit;

  @Column(name = "review_url")
  private String reviewUrl;

  @Column(name = "refund_conditions")
  private String refundConditions;

  @Column(name = "restart")
  private Boolean restart;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "organization_id", nullable = false)
  private Organization organization;

  public Challenge(String name, LocalDate startAt, LocalDate finishAt, Organization organization) {
    this.name = name;
    this.startAt = startAt;
    this.finishAt = finishAt;
    this.organization = organization;
  }

}