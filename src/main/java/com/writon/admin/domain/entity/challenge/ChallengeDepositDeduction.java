package com.writon.admin.domain.entity.challenge;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "challenge_deposit_deduction")
public class ChallengeDepositDeduction {

  @Id
  @Column(name = "challenge_deposit_deduction_id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Column(name = "start_count", columnDefinition = "int UNSIGNED not null")
  private Long startCount;

  @Column(name = "deduction_amount", columnDefinition = "int UNSIGNED not null")
  private Long deductionAmount;

  @Column(name = "end_count", columnDefinition = "int UNSIGNED not null")
  private Long endCount;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "challenge_id", nullable = false)
  private Challenge challenge;

}