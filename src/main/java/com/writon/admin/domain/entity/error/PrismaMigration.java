package com.writon.admin.domain.entity.error;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "_prisma_migrations")
public class PrismaMigration {

  @Id
  @Column(name = "id", nullable = false, length = 36)
  private String id;

  @Column(name = "checksum", nullable = false, length = 64)
  private String checksum;

  @Column(name = "finished_at")
  private Instant finishedAt;

  @Column(name = "migration_name", nullable = false)
  private String migrationName;

  @Lob
  @Column(name = "logs")
  private String logs;

  @Column(name = "rolled_back_at")
  private Instant rolledBackAt;

  @ColumnDefault("CURRENT_TIMESTAMP(3)")
  @Column(name = "started_at", nullable = false)
  private Instant startedAt;

  @ColumnDefault("'0'")
  @Column(name = "applied_steps_count", columnDefinition = "int UNSIGNED not null")
  private Long appliedStepsCount;

}