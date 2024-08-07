package com.writon.admin.domain.entity.activity;

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
public class CommentId implements Serializable {

  private static final long serialVersionUID = 6540381465933441948L;
  @Column(name = "comment_id", nullable = false)
  private Integer commentId;

  @Column(name = "user_templete_id", nullable = false)
  private Integer userTempleteId;

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
    CommentId entity = (CommentId) o;
    return Objects.equals(this.commentId, entity.commentId) &&
        Objects.equals(this.affiliationId, entity.affiliationId) &&
        Objects.equals(this.userTempleteId, entity.userTempleteId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(commentId, affiliationId, userTempleteId);
  }

}