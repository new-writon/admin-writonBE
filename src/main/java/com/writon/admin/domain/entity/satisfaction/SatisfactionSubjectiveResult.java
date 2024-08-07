package com.writon.admin.domain.entity.satisfaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
public class SatisfactionSubjectiveResult {

  @Id
  @Column(name = "satisfaction_subjective_result_id", nullable = false)
  private Integer id;

  @Column(name = "answer", nullable = false, length = 600)
  private String answer;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "satisfaction_id", nullable = false)
  private Satisfaction satisfaction;

  @ColumnDefault("CURRENT_TIMESTAMP(6)")
  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @ColumnDefault("CURRENT_TIMESTAMP(6)")
  @Column(name = "update_at", nullable = false)
  private Instant updateAt;

}