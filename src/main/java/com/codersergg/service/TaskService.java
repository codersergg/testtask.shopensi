package com.codersergg.service;

import com.codersergg.model.Clan;
import java.sql.SQLException;
import java.util.Optional;

// Еще один такой сервис
public class TaskService { // какой-то сервис с заданиями

  private final ClanService clans;

  public TaskService(ClanService clans) {
    this.clans = clans;
  }

  void completeTask(long clanId, long taskId) throws SQLException, InterruptedException {
    // ...

    // if (success)
    {
      Optional<Clan> clan = clans.getClan(clanId);
      // clan.[gold] += gold;
      // как-то сохранить изменения
    }
  }
}
