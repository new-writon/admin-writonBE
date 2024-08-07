package com.writon.admin.domain.entity.question;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
public class QuestionContent {

  @EmbeddedId
  private QuestionContentId id;

  @MapsId("questionId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "question_id", nullable = false)
  private Question question;

  @Lob
  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "visibility", nullable = false)
  private Boolean visibility = false;

  @ColumnDefault("CURRENT_TIMESTAMP(6)")
  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @ColumnDefault("CURRENT_TIMESTAMP(6)")
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

}