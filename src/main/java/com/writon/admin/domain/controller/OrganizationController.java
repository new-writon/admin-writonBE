package com.writon.admin.domain.controller;

import com.writon.admin.domain.dto.request.organization.CreateOrganizationRequestDto;
import com.writon.admin.domain.dto.request.organization.EditOrganizationRequestDto;
import com.writon.admin.domain.dto.response.organization.CreateOrganizationResponseDto;
import com.writon.admin.domain.dto.response.organization.EditOrganizationResponseDto;
import com.writon.admin.domain.service.ImageService;
import com.writon.admin.domain.service.OrganizationService;
import com.writon.admin.global.response.SuccessDto;
import java.util.List;
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

    // 로고를 그대로 유지하는 경우에는 requestDto에 logo 추가, file은 null
    String logoUrl = editOrganizationRequestDto.getLogo();
    System.out.println(file + logoUrl);
    if (file != null && !file.isEmpty()) {
      imageService.deleteImage();
      logoUrl = imageService.uploadImage(file);
    }

    // 로고를 삭제하고자 하는 경우에는 requestDto logo를 null로 전송, file도 null
    if (file == null && logoUrl == null) {
      System.out.println("로고 삭제 실행");
      imageService.deleteImage();
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

}


