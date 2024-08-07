package com.writon.admin.domain.entity.small_talk;

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
public class SmallTalkCommentId implements Serializable {

  private static final long serialVersionUID = 1069063857972007173L;
  @Column(name = "small_talk_comment_id", nullable = false)
  private Integer smallTalkCommentId;

  @Column(name = "small_talk_id", nullable = false)
  private Integer smallTalkId;

  @Column(name = "affiliation_id", nullable = false)
  private Integer affiliationId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    SmallTalkCommentId entity = (SmallTalkCommentId) o;
    return Objects.equals(this.smallTalkCommentId, entity.smallTalkCommentId) &&
        Objects.equals(this.affiliationId, entity.affiliationId) &&
        Objects.equals(this.smallTalkId, entity.smallTalkId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(smallTalkCommentId, affiliationId, smallTalkId);
  }

}