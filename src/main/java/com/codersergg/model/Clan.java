package com.codersergg.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Clan {

  private long id;     // id клана
  private String name; // имя клана
  private int gold;    // текущее количество золота в казне клана
}