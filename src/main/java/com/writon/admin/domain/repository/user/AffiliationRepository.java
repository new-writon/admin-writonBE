package com.writon.admin.domain.repository.user;

import com.writon.admin.domain.entity.user.Affiliation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AffiliationRepository extends JpaRepository<Affiliation, Long> {

}