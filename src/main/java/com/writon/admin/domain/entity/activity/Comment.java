package com.writon.admin.domain.entity.activity;

import com.writon.admin.domain.entity.user.Affiliation;
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
public class Comment {

  @EmbeddedId
  private CommentId id;

  @MapsId("userTempleteId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "user_templete_id", nullable = false)
  private UserTemplate userTemplete;

  @MapsId("affiliationId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "affiliation_id", nullable = false, referencedColumnName = "affiliation_id")
  private Affiliation affiliation;

  @Lob
  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "comment_group")
  private Integer commentGroup;

  @Column(name = "`check`")
  private Byte check;

  @ColumnDefault("CURRENT_TIMESTAMP(6)")
  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @ColumnDefault("CURRENT_TIMESTAMP(6)")
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

}