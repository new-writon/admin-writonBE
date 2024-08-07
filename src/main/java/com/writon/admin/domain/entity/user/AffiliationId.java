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
public class AffiliationId implements Serializable {

  private static final long serialVersionUID = -7613013071983109738L;
  @Column(name = "affiliation_id", nullable = false)
  private Integer affiliationId;

  @Column(name = "organization_id", nullable = false)
  private Integer organizationId;

  @Column(name = "user_id", nullable = false)
  private Integer userId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    AffiliationId entity = (AffiliationId) o;
    return Objects.equals(this.organizationId, entity.organizationId) &&
        Objects.equals(this.affiliationId, entity.affiliationId) &&
        Objects.equals(this.userId, entity.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(organizationId, affiliationId, userId);
  }

}