package com.writon.admin.domain.entity.activity;

import com.writon.admin.domain.entity.user.UserChallenge;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@Table(name = "user_templates")
@NoArgsConstructor
public class UserTemplate {

  @Id
  @Column(name = "user_template_id", columnDefinition = "int UNSIGNED not null")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "template_date")
  private LocalDate templateDate;

  @Column(name = "complete", nullable = false)
  private Boolean complete = false;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "user_challenge_id", nullable = false)
  private UserChallenge userChallenge;

}