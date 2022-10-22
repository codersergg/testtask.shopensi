package com.codersergg.service;

import com.codersergg.lock.LockService;
import java.sql.SQLException;

public class DeductGoldService {

  private final LockService lockService;
  private final ClanService clans;

  public DeductGoldService(LockService lockService, ClanService clans) {
    this.lockService = lockService;
    this.clans = clans;
  }

  public void deductGoldFromClan(long clanId, int gold)
      throws SQLException, InterruptedException {

    synchronized (lockService.getLock(clans.getClan(clanId).orElseThrow())) {
      try {
        clans.changeClansGold(clanId, -gold);
      } catch (SQLException e) {
        throw new RuntimeException();
      }
    }

  }
}
