package com.codersergg.service;

import com.codersergg.model.Clan;
import java.sql.SQLException;
import java.util.Optional;

// Так же у нас есть ряд сервисов похожих на эти.
// Их суть в том, что они добавляют(или уменьшают) золото в казне клана
public class UserAddGoldService { // пользователь добавляет золото из собственного кармана

  private final ClanService clans;

  public UserAddGoldService(ClanService clans) {
    this.clans = clans;
  }

  public void addGoldToClan(long userId, long clanId, int gold)
      throws SQLException, InterruptedException {
    Optional<Clan> clan = clans.getClan(clanId);
    int i = clan.orElseThrow().getGold() + gold;
    // как-то сохранить изменения
  }
}
