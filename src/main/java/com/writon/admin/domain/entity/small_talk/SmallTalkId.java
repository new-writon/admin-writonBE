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
public class SmallTalkId implements Serializable {

  private static final long serialVersionUID = 8776454903447519427L;
  @Column(name = "small_talk_id", nullable = false)
  private Integer smallTalkId;

  @Column(name = "user_challenge_id", nullable = false)
  private Integer userChallengeId;

  @Column(name = "challenge_id", nullable = false)
  private Integer challengeId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    SmallTalkId entity = (SmallTalkId) o;
    return Objects.equals(this.challengeId, entity.challengeId) &&
        Objects.equals(this.smallTalkId, entity.smallTalkId) &&
        Objects.equals(this.userChallengeId, entity.userChallengeId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(challengeId, smallTalkId, userChallengeId);
  }

}