package com.writon.admin.domain.entity.user;

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
public class UserChallengeId implements Serializable {

  private static final long serialVersionUID = -1533986089975187115L;
  @Column(name = "user_challenge_id", nullable = false)
  private Integer userChallengeId;

  @Column(name = "challenge_id", nullable = false)
  private Integer challengeId;

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
    UserChallengeId entity = (UserChallengeId) o;
    return Objects.equals(this.challengeId, entity.challengeId) &&
        Objects.equals(this.affiliationId, entity.affiliationId) &&
        Objects.equals(this.userChallengeId, entity.userChallengeId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(challengeId, affiliationId, userChallengeId);
  }

}