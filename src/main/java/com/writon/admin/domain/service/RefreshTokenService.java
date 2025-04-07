package com.writon.admin.domain.service;

import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RefreshTokenService {
  private final RedisTemplate<String, Object> redisTemplate;
  private final Long REFRESH_TOKEN_EXPIRE_TIME = 60 * 24L; // TTL: 1일(24시간)

  public void saveRefreshToken(String email, String refreshToken) {
    redisTemplate.opsForValue().set(email, refreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MINUTES); // 이메일을 key로 저장
  }

  public String getRefreshToken(String email) {
    Object refreshToken = redisTemplate.opsForValue().get(email);
    return refreshToken == null ? null : (String) refreshToken;
  }

  public void deleteRefreshToken(String email) {
    redisTemplate.delete(email);
  }

  public boolean hasKey(String email) {
    Boolean hasKey = redisTemplate.hasKey(email);
    return hasKey != null && hasKey;
  }
}
