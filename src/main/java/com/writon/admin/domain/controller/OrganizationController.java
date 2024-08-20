package com.writon.admin.domain.controller;

import com.writon.admin.domain.dto.request.organization.CreateOrganizationRequestDto;
import com.writon.admin.domain.dto.request.organization.EditOrganizationRequestDto;
import com.writon.admin.domain.dto.response.organization.CreateOrganizationResponseDto;
import com.writon.admin.domain.service.OrganizationService;
import com.writon.admin.global.response.SuccessDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/organization")
@RequiredArgsConstructor
public class OrganizationController {

  private final OrganizationService organizationService;

  @PostMapping
  public SuccessDto<CreateOrganizationResponseDto> createOrganization(
      @RequestPart(required = false) MultipartFile file,
      @RequestPart CreateOrganizationRequestDto createOrganizationRequestDto
  ) {

    String logoUrl = null;
    if (file != null && !file.isEmpty()) {
      logoUrl = organizationService.uploadLogo(file);
    }

    CreateOrganizationResponseDto createOrganizationResponseDto
        = organizationService.createOrganization(createOrganizationRequestDto, logoUrl);

    return new SuccessDto<>(new CreateResponseDto(
  }

  @GetMapping("/position")
  public SuccessDto<List<String>> getPositions() {
    return new SuccessDto<>(organizationService.getPositions());
  }

}


