package com.writon.admin.domain.service;

import com.writon.admin.domain.entity.organization.AdminUser;
import com.writon.admin.domain.repository.organization.AdminUserRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final AdminUserRepository adminUserRepository;

  @Override
  public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
    return adminUserRepository.findByIdentifier(identifier)
        .map(this::createUserDetails)
        .orElseThrow(() -> new UsernameNotFoundException(identifier + " -> 데이터베이스에서 찾을 수 없습니다."));
  }

  // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
  private UserDetails createUserDetails(AdminUser adminUser) {
    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("admin");

    return new User(
        adminUser.getIdentifier(),
        adminUser.getPassword(),
        Collections.singleton(grantedAuthority)
    );
  }
}
