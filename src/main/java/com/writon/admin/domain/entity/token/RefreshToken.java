package com.writon.admin.domain.entity.token;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "refreshToken", timeToLive = 7 * 24 * 60 * 60 * 1000L) // 60 * 60 * 1000L (1시간)
public class RefreshToken {

  @Id
  private String token;

  @Indexed
  private String identifier;

  @Builder
  public RefreshToken(String token, String identifier) {
    this.token = token;
    this.identifier = identifier;
  }

  public RefreshToken updateToken(String token) {
    this.token = token;
    return this;
  }
}