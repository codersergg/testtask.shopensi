package com.codersergg.monitoring.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Event implements Serializable {

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
    if (!(o instanceof Event)) {
      return false;
    }

    Event event = (Event) o;

    if (getClanId() != event.getClanId()) {
      return false;
    }
    if (getAmountGoldBeforeRaise() != event.getAmountGoldBeforeRaise()) {
      return false;
    }
    if (getAmountGoldToRaise() != event.getAmountGoldToRaise()) {
      return false;
    }
    if (getAmountGoldAfterRaise() != event.getAmountGoldAfterRaise()) {
      return false;
    }
    if (!getDateTime().equals(event.getDateTime())) {
      return false;
    }
    if (getOperation() != event.getOperation()) {
      return false;
    }
    return getMessage().equals(event.getMessage());
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
