package com.writon.admin.domain.service;

import com.writon.admin.domain.dto.request.organization.CreateOrganizationRequestDto;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.writon.admin.domain.dto.response.organization.CreateOrganizationResponseDto;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.writon.admin.domain.entity.organization.AdminUser;
import com.writon.admin.domain.entity.organization.Organization;
import com.writon.admin.domain.entity.organization.Position;
import com.writon.admin.domain.repository.organization.AdminUserRepository;
import com.writon.admin.domain.repository.organization.OrganizationRepository;
import com.writon.admin.domain.repository.organization.PositionRepository;
import com.writon.admin.domain.util.TokenUtil;
import com.writon.admin.global.error.CustomException;
import com.writon.admin.global.error.ErrorCode;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class OrganizationService {

  private final OrganizationRepository organizationRepository;
  private final AdminUserRepository adminUserRepository;
  private final AmazonS3 amazonS3Client;
  private final TokenUtil tokenUtil;


  // ========== CreateOrganization API ==========
  public CreateOrganizationResponseDto createOrganization(
      CreateOrganizationRequestDto createOrganizationRequestDto,
      String logoUrl
  ) {
    // 1. 사용자 정보 불러오기
    AdminUser adminUser = tokenUtil.getAdminUser();

    // 2. Organization 객체 생성
    Organization organization = new Organization(
        createOrganizationRequestDto.getName(),
        logoUrl,
        createOrganizationRequestDto.getThemeColor(),
        adminUser
    );

    // 3. Organization 저장
    Organization savedOrganization = organizationRepository.save(organization);

    // 4. Position 저장
    for (String position : createOrganizationRequestDto.getPositions()) {
      Position positionEntity = new Position(position, organization);
      positionRepository.save(positionEntity);
    }

    return new CreateOrganizationResponseDto(
        savedOrganization.getId(),
        savedOrganization.getName(),
        savedOrganization.getThemeColor(),
        savedOrganization.getLogo()
    );
  }

  // ========== GetPositions API ==========
  public List<String> getPositions() {
    Organization organization = tokenUtil.getOrganization();

    List<Position> responseDto = positionRepository.findByOrganizationId(organization.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

    return responseDto.stream()
        .map(Position::getName)
        .toList();
  }
    metadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);

    String fileName = file.getOriginalFilename();

    // 2. Amazon S3에 이미지 등록
    try {
      amazonS3Client.putObject(
          new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata)
              .withCannedAcl(CannedAccessControlList.PublicRead)
      );
    } catch (IOException e) {
      throw new CustomException(ErrorCode.BAD_REQUEST);
  // ========== EditPositions API ==========
  public List<String> editPositions(List<String> positionList) {
    Organization organization = tokenUtil.getOrganization();

    // 1. 현재 조직의 모든 Position을 조회
    List<Position> existingPositions = positionRepository.findByOrganizationId(organization.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

    // 2. 기존의 position names 리스트를 생성
    List<String> existingPositionNames = existingPositions.stream()
        .map(Position::getName)
        .toList();

    // 3. 새로운 position names 중에서 기존에 없으면 추가
    for (String newPositionName : positionList) {
      if (!existingPositionNames.contains(newPositionName)) {
        Position newPosition = new Position();
        newPosition.setName(newPositionName);
        newPosition.setOrganization(organization);

        positionRepository.save(newPosition);
      }
    }

    // 4. 기존의 position names 중에서 새로운 리스트에 없으면 삭제
    for (Position existingPosition : existingPositions) {
      if (!positionList.contains(existingPosition.getName())) {
        positionRepository.delete(existingPosition);
      }
    }

    return getPositions();
  }

}
