package com.writon.admin.domain.repository.challenge;

import com.writon.admin.domain.entity.challenge.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {

}