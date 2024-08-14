package com.writon.admin.domain.entity.question;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "questions")
@NoArgsConstructor
public class Question {

  @Id
  @Column(name = "question_id", columnDefinition = "int UNSIGNED not null")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "question", nullable = false)
  private String question;

  @Column(name = "category", nullable = false, length = 10)
  private String category;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "challenge_id", nullable = false)
  private Challenge challenge;

  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "keyword_id")
  private Keyword keyword;

  // BasicQuestion
  public Question(String question, String category, Challenge challenge) {
    this.question = question;
    this.category = category;
    this.challenge = challenge;
  }

  // SpecialQuestion
  public Question(String question, String category, Challenge challenge, Keyword keyword) {
    this.question = question;
    this.category = category;
    this.challenge = challenge;
    this.keyword = keyword;
  }
}