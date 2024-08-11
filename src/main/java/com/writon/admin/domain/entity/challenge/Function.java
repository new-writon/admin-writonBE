package com.writon.admin.domain.entity.challenge;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "function")
public class Function {

  @Id
  @Column(name = "function_id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

}