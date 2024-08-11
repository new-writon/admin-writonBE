package com.writon.admin.domain.entity.small_talk;

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
@Table(name = "small_talk")
public class SmallTalk {

  @Id
  @Column(name = "small_talk_id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Column(name = "question", nullable = false)
  private String question;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "user_challenge_id", nullable = false)
  private UserChallenge userChallenge;

}