package com.writon.admin.domain.controller;

import com.writon.admin.domain.dto.request.organization.CreateOrganizationRequestDto;
import com.writon.admin.domain.dto.request.organization.EditOrganizationRequestDto;
import com.writon.admin.domain.dto.response.organization.CreateOrganizationResponseDto;
import com.writon.admin.domain.dto.response.organization.EditOrganizationResponseDto;
import com.writon.admin.domain.entity.organization.Organization;
import com.writon.admin.domain.service.ImageService;
import com.writon.admin.domain.service.OrganizationService;
import com.writon.admin.domain.util.TokenUtil;
import com.writon.admin.global.response.SuccessDto;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/organization")
@RequiredArgsConstructor
public class OrganizationController {

  private final OrganizationService organizationService;
  private final ImageService imageService;
  private final TokenUtil tokenUtil;

  private static final String DEFAULT_LOGO_URL = "https://writon-data.s3.ap-northeast-2.amazonaws.com/logo/default-logo.png";

  @PostMapping
  public SuccessDto<CreateOrganizationResponseDto> createOrganization(
      @RequestPart(required = false) MultipartFile file,
      @RequestPart CreateOrganizationRequestDto createOrganizationRequestDto
  ) {

    String logoUrl = null;
    if (file != null && !file.isEmpty()) {
      logoUrl = imageService.uploadImage(file);
    }

    CreateOrganizationResponseDto createOrganizationResponseDto
        = organizationService.createOrganization(createOrganizationRequestDto, logoUrl);

    return new SuccessDto<>(createOrganizationResponseDto);
  }

  @GetMapping("/position")
  public SuccessDto<List<String>> getPositions() {
    return new SuccessDto<>(organizationService.getPositions());
  }

  @PatchMapping("/info")
  public SuccessDto<EditOrganizationResponseDto> editOrganization(
      @RequestPart(required = false) MultipartFile file,
      @RequestPart EditOrganizationRequestDto editOrganizationRequestDto
  ) {
    String logoUrl = editOrganizationRequestDto.getLogo();

    // 로고를 그대로 유지하는 경우에는 requestDto에 logo 추가, file은 null
    if (file != null && !file.isEmpty()) {
      deleteImage();
      logoUrl = imageService.uploadImage(file);
    }

    // 로고를 삭제하고자 하는 경우에는 requestDto logo를 null로 전송, file도 null
    if (file == null &&
        (Objects.equals(logoUrl, "") || Objects.equals(logoUrl, DEFAULT_LOGO_URL))) {
      deleteImage();
    }

    EditOrganizationResponseDto editOrganizationResponseDto
        = organizationService.editOrganization(editOrganizationRequestDto, logoUrl);

    return new SuccessDto<>(editOrganizationResponseDto);
  }

  @PatchMapping("/position")
  public SuccessDto<List<String>> editPositions(
      @RequestBody List<String> positionList
  ) {
    List<String> savedPositionList = organizationService.editPositions(positionList);

    return new SuccessDto<>(savedPositionList);
  }

  private void deleteImage() {
    Organization organization = tokenUtil.getOrganization();
    String imageUrl = organization.getLogo();

    if (!Objects.equals(imageUrl, "") &&
        !Objects.equals(imageUrl, DEFAULT_LOGO_URL)) {
      imageService.deleteImage(imageUrl);
    }
  }
}


