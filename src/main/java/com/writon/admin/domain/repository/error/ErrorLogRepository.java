package com.writon.admin.domain.repository.error;

import com.writon.admin.domain.entity.error.ErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {

}