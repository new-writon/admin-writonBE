package com.writon.admin.domain.entity.user;

import com.writon.admin.domain.entity.challenge.Challenge;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
public class UserChallenge {

  @EmbeddedId
  private UserChallengeId id;

  @MapsId("challengeId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "challenge_id", nullable = false)
  private Challenge challenge;

  @MapsId("affiliationId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "affiliation_id", nullable = false, referencedColumnName = "affiliation_id")
  private Affiliation affiliation;

  @Column(name = "user_deposit", nullable = false)
  private Integer userDeposit;

  @Column(name = "review", nullable = false)
  private Byte review;

  @Column(name = "check_count")
  private Integer checkCount;

  @Column(name = "re_engagement")
  private Byte reEngagement;

  @Column(name = "cheering_phrase")
  private String cheeringPhrase;

  @Column(name = "cheering_phrase_date")
  private LocalDate cheeringPhraseDate;

  @ColumnDefault("CURRENT_TIMESTAMP(6)")
  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @ColumnDefault("CURRENT_TIMESTAMP(6)")
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

}