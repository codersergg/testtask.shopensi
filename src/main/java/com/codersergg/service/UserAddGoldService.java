package com.codersergg.service;

import com.codersergg.lock.LockService;
import com.codersergg.monitoring.Monitoring;
import com.codersergg.monitoring.model.GoldValues;
import com.codersergg.monitoring.model.Metric;
import com.codersergg.monitoring.model.Operation;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

// Так же у нас есть ряд сервисов похожих на эти.
// Их суть в том, что они добавляют(или уменьшают) золото в казне клана
public class UserAddGoldService { // пользователь добавляет золото из собственного кармана

  private final LockService lockService;
  private final ClanService clans;
  private final UserService users;
  private final Monitoring monitoring;
  private final ExecutorService executor;

  public UserAddGoldService(LockService lockService, ClanService clans, UserService users,
      Monitoring monitoring, ExecutorService executor) {
    this.lockService = lockService;
    this.clans = clans;
    this.users = users;
    this.monitoring = monitoring;
    this.executor = executor;
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
          GoldValues goldValues = clans.changeClansGold(clanId, gold);

          executor.submit(() ->
              monitoring.send(Metric.builder()
                  .dateTime(goldValues.getDateTime())
                  .clanId(clanId)
                  .amountGoldBeforeRaise(goldValues.getAmountGoldBeforeRaise())
                  .amountGoldToRaise(goldValues.getAmountGoldToRaise())
                  .amountGoldAfterRaise(goldValues.getAmountGoldAfterRaise())
                  .operation(Operation.ADD)
                  .message("userId: " + userId)
                  .build()));

        } catch (SQLException e) {
          users.changeUsersGold(userId, gold);
          throw new RuntimeException();
        }
      }
    }
  }
}
