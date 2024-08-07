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
public class LikeId implements Serializable {

  private static final long serialVersionUID = -9173024958761326269L;
  @Column(name = "like_id", nullable = false)
  private Integer likeId;

  @Column(name = "affiliation_id", nullable = false)
  private Integer affiliationId;

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
    LikeId entity = (LikeId) o;
    return Objects.equals(this.likeId, entity.likeId) &&
        Objects.equals(this.affiliationId, entity.affiliationId) &&
        Objects.equals(this.userTempleteId, entity.userTempleteId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(likeId, affiliationId, userTempleteId);
  }

}