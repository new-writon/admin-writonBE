package com.writon.admin.domain.entity.question;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "keywords")
public class Keyword {

  @Id
  @Column(name = "keyword_id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Column(name = "keyword", nullable = false)
  private String keyword;

}