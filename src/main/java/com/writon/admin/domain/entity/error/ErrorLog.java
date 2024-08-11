package com.writon.admin.domain.entity.error;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "error_logs")
public class ErrorLog {

  @Id
  @Column(name = "error_log_id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Column(name = "level", nullable = false, length = 10)
  private String level;

  @Column(name = "timestamp", nullable = false, length = 45)
  private String timestamp;

  @Column(name = "message", nullable = false, length = 500)
  private String message;

}