package com.codersergg.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Task {

  private long id;       // id таски
  private String name;   // имя таски
  private int gold;      // вознаграждение
  private int progress;  // прогресс выподнения

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Task)) {
      return false;
    }

    Task task = (Task) o;

    if (getId() != task.getId()) {
      return false;
    }
    return getName().equals(task.getName());
  }

  @Override
  public int hashCode() {
    int result = (int) (getId() ^ (getId() >>> 32));
    result = 31 * result + getName().hashCode();
    return result;
  }
}
