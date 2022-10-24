package com.codersergg.service;

import com.codersergg.lock.LockService;
import com.codersergg.monitoring.Monitoring;
import com.codersergg.monitoring.model.GoldValues;
import com.codersergg.monitoring.model.Metric;
import com.codersergg.monitoring.model.Operation;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

public class DeductGoldService {

  private final LockService lockService;
  private final ClanService clans;
  private final Monitoring monitoring;
  private final ExecutorService executor;

  public DeductGoldService(LockService lockService, ClanService clans, Monitoring monitoring,
      ExecutorService executor) {
    this.lockService = lockService;
    this.clans = clans;
    this.monitoring = monitoring;
    this.executor = executor;
  }

  public void deductGoldFromClan(long clanId, int gold)
      throws SQLException, InterruptedException {

    synchronized (lockService.getLock(clans.getClan(clanId).orElseThrow())) {
      GoldValues goldValues = clans.changeClansGold(clanId, -gold);

      executor.submit(() -> monitoring.send(Metric.builder()
          .dateTime(goldValues.getDateTime())
          .clanId(clanId)
          .amountGoldBeforeRaise(goldValues.getAmountGoldBeforeRaise())
          .amountGoldToRaise(goldValues.getAmountGoldToRaise())
          .amountGoldAfterRaise(goldValues.getAmountGoldAfterRaise())
          .operation(Operation.DEDUCT)
          .message("deductGoldFromClan")
          .build()));
    }
  }
}
