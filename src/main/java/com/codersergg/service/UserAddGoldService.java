package com.codersergg.service;

import com.codersergg.lock.LockService;
import com.codersergg.model.Clan;
import com.codersergg.model.User;
import java.sql.SQLException;
import java.util.Optional;

// Так же у нас есть ряд сервисов похожих на эти.
// Их суть в том, что они добавляют(или уменьшают) золото в казне клана
public class UserAddGoldService { // пользователь добавляет золото из собственного кармана

  private final LockService lockService;
  private final ClanService clans;
  private final UserService users;

  public UserAddGoldService(LockService lockService, ClanService clans, UserService users) {
    this.lockService = lockService;
    this.clans = clans;
    this.users = users;
  }

  public void addGoldToClan(long userId, long clanId, int gold)
      throws SQLException, InterruptedException {

    synchronized (lockService.getLock(clans.getClan(clanId).orElseThrow())) {
      Optional<User> user = users.getUser(userId);
      int userGold = user.orElseThrow().getGold() - gold;
      users.changeUsersGold(userId, userGold);

      synchronized (lockService.getLock(users.getUser(userId).orElseThrow())) {
        Optional<Clan> clan = clans.getClan(clanId);
        int clanGold = clan.orElseThrow().getGold() + gold;
        clans.changeClansGold(clanId, clanGold);
      }
    }
  }
}