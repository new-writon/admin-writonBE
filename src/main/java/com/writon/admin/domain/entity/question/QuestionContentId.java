package com.writon.admin.domain.entity.question;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

@Getter
@Setter
@Embeddable
public class QuestionContentId implements Serializable {

  private static final long serialVersionUID = -6313009082327344283L;
  @Column(name = "question_content_id", nullable = false)
  private Integer questionContentId;

  @Column(name = "question_id", nullable = false)
  private Integer questionId;

  @Column(name = "user_templete_id", nullable = false)
  private Integer userTempleteId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    QuestionContentId entity = (QuestionContentId) o;
    return Objects.equals(this.questionId, entity.questionId) &&
        Objects.equals(this.questionContentId, entity.questionContentId) &&
        Objects.equals(this.userTempleteId, entity.userTempleteId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questionId, questionContentId, userTempleteId);
  }

}