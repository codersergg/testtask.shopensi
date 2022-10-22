package com.codersergg.service;

import com.codersergg.lock.LockService;
import java.sql.SQLException;

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
    synchronized (lockService.getLock(users.getUser(userId).orElseThrow())) {
      try {
        users.changeUsersGold(userId, -gold);
      } catch (SQLException e) {
        throw new RuntimeException();
      }
      synchronized (lockService.getLock(clans.getClan(clanId).orElseThrow())) {
        try {
          clans.changeClansGold(clanId, gold);
        } catch (SQLException e) {
          users.changeUsersGold(userId, gold);
          throw new RuntimeException();
        }
      }
    }
  }
}
