package com.codersergg.service;

import com.codersergg.model.ApplicationLock;
import com.codersergg.model.Clan;
import com.codersergg.model.User;
import java.sql.SQLException;
import java.util.Optional;

// Так же у нас есть ряд сервисов похожих на эти.
// Их суть в том, что они добавляют(или уменьшают) золото в казне клана
public class UserAddGoldService { // пользователь добавляет золото из собственного кармана

  private final ApplicationLock applicationLock;
  private final ClanService clans;
  private final UserService users;

  public UserAddGoldService(ApplicationLock applicationLock, ClanService clans, UserService users) {
    this.applicationLock = applicationLock;
    this.clans = clans;
    this.users = users;
  }

  public void addGoldToClan(long userId, long clanId, int gold)
      throws SQLException, InterruptedException {

    synchronized (applicationLock.getLock(clans.getClan(clanId).orElseThrow())) {
      Optional<User> user = users.getUser(userId);
      int userGold = user.orElseThrow().getGold() - gold;
      users.changeUsersGold(userId, userGold);

      synchronized (applicationLock.getLock(users.getUser(userId).orElseThrow())) {
        Optional<Clan> clan = clans.getClan(clanId);
        int clanGold = clan.orElseThrow().getGold() + gold;
        clans.changeClansGold(clanId, clanGold);
      }
    }
  }
}