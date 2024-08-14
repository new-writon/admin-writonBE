package com.writon.admin.domain.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.writon.admin.domain.dto.request.organization.CreateRequestDto;
import com.writon.admin.domain.dto.response.organization.CreateResponseDto;
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


  // ========== Create API ==========
  public CreateResponseDto create(CreateRequestDto createRequestDto) {
    // 1. 사용자 정보 불러오기
    AdminUser adminUser = tokenUtil.getAdminUser();

    // 2. Organization 객체 생성
    Organization organization = new Organization(
        createRequestDto.getName(),
        null,
        createRequestDto.getThemeColor(),
        adminUser
    );

    // 3. Organization 저장
    Organization savedOrganization = organizationRepository.save(organization);

    // 4. Position 저장
    for (String position : createRequestDto.getPositions()) {
      Position positionEntity = new Position(position, organization);
      positionRepository.save(positionEntity);
    }

    return new CreateResponseDto(
        savedOrganization.getId(),
        savedOrganization.getName(),
        savedOrganization.getLogo()
    );
  }

  // ========== Upload API ==========
  @Value("${cloud.aws.s3.bucket}")
  private String bucket;
  private final PositionRepository positionRepository;

  public String uploadLogo(MultipartFile file) {

    // 1. metadata 생성
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(file.getSize());
    metadata.setContentType(file.getContentType());
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
    }

    // 3. Image Url 제공
    return amazonS3Client.getUrl(bucket, fileName).toString();

  }
}
