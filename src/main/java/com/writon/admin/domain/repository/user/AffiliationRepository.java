package com.writon.admin.domain.repository.user;

import com.writon.admin.domain.entity.user.Affiliation;
import com.writon.admin.domain.entity.user.AffiliationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AffiliationRepository extends JpaRepository<Affiliation, AffiliationId> {

}