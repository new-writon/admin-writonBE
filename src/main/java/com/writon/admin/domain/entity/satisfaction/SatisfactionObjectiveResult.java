package com.writon.admin.domain.entity.satisfaction;

import com.writon.admin.domain.entity.user.UserChallenge;
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
@Table(name = "satisfaction_objective_result")
public class SatisfactionObjectiveResult {

  @Id
  @Column(name = "satisfaction_objective_result_id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Column(name = "score", columnDefinition = "int UNSIGNED not null")
  private Long score;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "satisfaction_id", nullable = false)
  private Satisfaction satisfaction;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "user_challenge_id", nullable = false)
  private UserChallenge userChallenge;

}