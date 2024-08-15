package com.writon.admin.domain.entity.user;

import com.writon.admin.domain.entity.challenge.Challenge;
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
@Table(name = "user_challenges")
@NoArgsConstructor
public class UserChallenge {

  @Id
  @Column(name = "user_challenge_id", columnDefinition = "int UNSIGNED not null")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_deposit", columnDefinition = "int UNSIGNED not null")
  private Long userDeposit;

  @Column(name = "review", nullable = false)
  private Boolean review = false;

  @Column(name = "check_count", columnDefinition = "int UNSIGNED")
  private Long checkCount;

  @Column(name = "re_engagement")
  private Boolean reEngagement;

  @Column(name = "cheering_phrase")
  private String cheeringPhrase;

  @Column(name = "cheering_phrase_date")
  private LocalDate cheeringPhraseDate;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "challenge_id", nullable = false)
  private Challenge challenge;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "affiliation_id", nullable = false)
  private Affiliation affiliation;

}