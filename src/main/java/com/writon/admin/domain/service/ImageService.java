package com.writon.admin.domain.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.writon.admin.domain.util.TokenUtil;
import com.writon.admin.global.error.CustomException;
import com.writon.admin.global.error.ErrorCode;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

  private final TokenUtil tokenUtil;
  @Value("${cloud.aws.s3.bucket}")
  private String bucket;
  private final AmazonS3 amazonS3Client;

  // ========== UploadImage API ==========
  public String uploadImage(MultipartFile file) {

    // 1. metadata 생성
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(file.getSize());
    metadata.setContentType(file.getContentType());
    metadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);

    // 2. 파일명 생성
    String uuid = UUID.randomUUID().toString();
    String[] parts = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
    String extension = parts[parts.length - 1];
    String fileName = "logo/" + uuid + "." + extension;

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

  // ========== DeleteImage API ==========
  public void deleteImage(String imageUrl) {
    String splitStr = ".com/";

    if (imageUrl != null) {
      String key = imageUrl.substring(imageUrl.lastIndexOf(splitStr) + splitStr.length());
      amazonS3Client.deleteObject(bucket, key);
    }
  }

}
