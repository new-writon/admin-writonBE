package com.writon.admin.domain.entity.satisfaction;

import com.writon.admin.domain.entity.challenge.Challenge;
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
@Table(name = "satisfaction")
public class Satisfaction {

  @Id
  @Column(name = "satisfaction_id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Column(name = "question", nullable = false)
  private String question;

  @Column(name = "type", nullable = false)
  private String type;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "challenge_id", nullable = false)
  private Challenge challenge;

}