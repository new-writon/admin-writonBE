package com.writon.admin.domain.entity.challenge;

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
@Table(name = "challenge_day")
public class ChallengeDay {

  @Id
  @Column(name = "challenge_day_id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Column(name = "day", nullable = false)
  private LocalDate day;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "challenge_id", nullable = false)
  private Challenge challenge;

}