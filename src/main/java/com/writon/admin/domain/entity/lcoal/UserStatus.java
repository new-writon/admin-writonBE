package com.writon.admin.domain.entity.lcoal;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserStatus {

  private String name;
  private List<Status> statusList;
}