package com.writon.admin.domain.entity.question;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "keywords")
@NoArgsConstructor
public class Keyword {

  @Id
  @Column(name = "keyword_id", columnDefinition = "int UNSIGNED not null")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "keyword", nullable = false)
  private String keyword;

  public Keyword(String keyword) {
    this.keyword = keyword;
  }
}