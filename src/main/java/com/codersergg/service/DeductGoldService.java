package com.codersergg.service;

import com.codersergg.model.Clan;
import java.sql.SQLException;
import java.util.Optional;

public class DeductGoldService {

  private final ClanService clans;

  public DeductGoldService(ClanService clans) {
    this.clans = clans;
  }

  public void deductGoldFromClan(long userId, long clanId, int gold)
      throws SQLException, InterruptedException {
    Optional<Clan> clan = clans.getClan(clanId);
    // clan.[gold] += gold;
    // как-то сохранить изменения
  }
}
