package com.writon.admin.domain.controller;

import com.writon.admin.domain.dto.request.organization.CreateRequestDto;
import com.writon.admin.domain.dto.response.organization.CreateResponseDto;
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
  public SuccessDto<CreateResponseDto> create(
      @RequestPart(required = false) MultipartFile file,
      @RequestPart CreateRequestDto createRequestDto
  ) {

    String logoUrl = null;
    if (file != null && !file.isEmpty()) {
      logoUrl = organizationService.uploadLogo(file);
    }

    CreateResponseDto createResponseDto = organizationService.create(createRequestDto);

    return new SuccessDto<>(new CreateResponseDto(
        createResponseDto.getOrganizationId(),
        createResponseDto.getOrganizationName(),
        logoUrl
    )
    );
  }

}


