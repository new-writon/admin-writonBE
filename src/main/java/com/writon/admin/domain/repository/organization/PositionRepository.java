package com.writon.admin.domain.repository.organization;

import com.writon.admin.domain.entity.organization.Position;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {

  List<Position> findByOrganizationId(Long organizationId);

}