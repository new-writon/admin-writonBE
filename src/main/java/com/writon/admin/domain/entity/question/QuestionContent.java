package com.writon.admin.domain.entity.question;

import com.writon.admin.domain.entity.activity.UserTemplate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "question_contents")
public class QuestionContent {

  @Id
  @Column(name = "question_content_id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Lob
  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "visibility", nullable = false)
  private Boolean visibility = false;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "question_id", nullable = false)
  private Question question;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "user_template_id", nullable = false)
  private UserTemplate userTemplate;

}