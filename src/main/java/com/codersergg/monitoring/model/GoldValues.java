package com.codersergg.monitoring.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoldValues {

  private LocalDateTime dateTime;
  private int amountGoldBeforeRaise;
  private int amountGoldToRaise;
  private int amountGoldAfterRaise;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof GoldValues)) {
      return false;
    }

    GoldValues that = (GoldValues) o;

    if (getAmountGoldBeforeRaise() != that.getAmountGoldBeforeRaise()) {
      return false;
    }
    if (getAmountGoldToRaise() != that.getAmountGoldToRaise()) {
      return false;
    }
    if (getAmountGoldAfterRaise() != that.getAmountGoldAfterRaise()) {
      return false;
    }
    return getDateTime().equals(that.getDateTime());
  }

  @Override
  public int hashCode() {
    int result = getDateTime().hashCode();
    result = 31 * result + getAmountGoldBeforeRaise();
    result = 31 * result + getAmountGoldToRaise();
    result = 31 * result + getAmountGoldAfterRaise();
    return result;
  }
}
