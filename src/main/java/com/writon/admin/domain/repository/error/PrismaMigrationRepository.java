package com.writon.admin.domain.repository.error;

import com.writon.admin.domain.entity.error.PrismaMigration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrismaMigrationRepository extends JpaRepository<PrismaMigration, String> {

}