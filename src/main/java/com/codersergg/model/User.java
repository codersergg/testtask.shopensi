package com.codersergg.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

  private long id;     // id user
  private String name; // имя user
  private int gold;    // текущее количество золота user
  private long clanId; // имя клана
}
