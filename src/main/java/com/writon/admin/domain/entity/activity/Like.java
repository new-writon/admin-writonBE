package com.writon.admin.domain.entity.activity;

import com.writon.admin.domain.entity.user.Affiliation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "likes")
public class Like {

  @Id
  @Column(name = "like_id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Column(name = "`check`", nullable = false)
  private Boolean check = false;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "affiliation_id", nullable = false)
  private Affiliation affiliation;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "user_template_id", nullable = false)
  private UserTemplate userTemplate;

}