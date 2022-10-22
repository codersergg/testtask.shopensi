package com.codersergg.monitoring.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Metric {

  private LocalDateTime dateTime;
  private long clanId;
  private int amountGoldBeforeRaise;
  private int amountGoldToRaise;
  private int amountGoldAfterRaise;
  private Operation operation;
  private String message;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Metric)) {
      return false;
    }

    Metric metric = (Metric) o;

    if (getClanId() != metric.getClanId()) {
      return false;
    }
    if (getAmountGoldBeforeRaise() != metric.getAmountGoldBeforeRaise()) {
      return false;
    }
    if (getAmountGoldToRaise() != metric.getAmountGoldToRaise()) {
      return false;
    }
    if (getAmountGoldAfterRaise() != metric.getAmountGoldAfterRaise()) {
      return false;
    }
    if (!getDateTime().equals(metric.getDateTime())) {
      return false;
    }
    if (getOperation() != metric.getOperation()) {
      return false;
    }
    return getMessage().equals(metric.getMessage());
  }

  @Override
  public int hashCode() {
    int result = getDateTime().hashCode();
    result = 31 * result + (int) (getClanId() ^ (getClanId() >>> 32));
    result = 31 * result + getAmountGoldBeforeRaise();
    result = 31 * result + getAmountGoldToRaise();
    result = 31 * result + getAmountGoldAfterRaise();
    result = 31 * result + getOperation().hashCode();
    result = 31 * result + getMessage().hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Metric{" +
        "dateTime=" + dateTime +
        ", clanId=" + clanId +
        ", amountGoldBeforeRaise=" + amountGoldBeforeRaise +
        ", amountGoldToRaise=" + amountGoldToRaise +
        ", amountGoldAfterRaise=" + amountGoldAfterRaise +
        ", operation=" + operation +
        ", message='" + message + '\'' +
        '}';
  }
}
