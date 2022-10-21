package com.codersergg.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Clan {

  private long id;     // id клана
  private String name; // имя клана
  private int gold;    // текущее количество золота в казне клана

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Clan)) {
      return false;
    }

    Clan clan = (Clan) o;

    if (getId() != clan.getId()) {
      return false;
    }
    return getName().equals(clan.getName());
  }

  @Override
  public int hashCode() {
    int result = (int) (getId() ^ (getId() >>> 32));
    result = 31 * result + getName().hashCode();
    return result;
  }
}