package com.writon.admin.domain.entity.challenge;

import com.writon.admin.domain.entity.organization.Organization;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
public class Challenge {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "challenge_id", nullable = false)
  private Integer id;

  @Column(name = "name", nullable = false, length = 40)
  private String name;

  @ColumnDefault("CURRENT_TIMESTAMP(6)")
  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @ColumnDefault("CURRENT_TIMESTAMP(6)")
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @Column(name = "start_at", nullable = false)
  private LocalDate startAt;

  @Column(name = "finish_at", nullable = false)
  private LocalDate finishAt;

  @Column(name = "deposit", nullable = false)
  private Integer deposit;

  @Lob
  @Column(name = "review_url")
  private String reviewUrl;

  @Lob
  @Column(name = "refund_conditions")
  private String refundConditions;

  @Column(name = "restart")
  private Byte restart;

  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "organization_id")
  private Organization organization;

}