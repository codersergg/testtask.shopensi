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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof User)) {
      return false;
    }

    User user = (User) o;

    if (getId() != user.getId()) {
      return false;
    }
    return getName().equals(user.getName());
  }

  @Override
  public int hashCode() {
    int result = (int) (getId() ^ (getId() >>> 32));
    result = 31 * result + getName().hashCode();
    return result;
  }
}
