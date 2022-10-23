package com.codersergg.utils;

import com.codersergg.model.Clan;
import com.codersergg.model.Task;
import com.codersergg.model.User;
import com.codersergg.service.ClanService;
import com.codersergg.service.DeductGoldService;
import com.codersergg.service.TaskService;
import com.codersergg.service.UserAddGoldService;
import com.codersergg.service.UserService;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import lombok.extern.java.Log;

@Log
public class PopulateDB {

  private final ExecutorService executor;
  private final UserService users;
  private final ClanService clans;
  private final TaskService tasks;
  private final UserAddGoldService userAddGoldService;
  private final DeductGoldService deductGoldService;

  public PopulateDB(ExecutorService executor, UserService users, ClanService clans,
      TaskService tasks, UserAddGoldService userAddGoldService,
      DeductGoldService deductGoldService) {
    this.executor = executor;
    this.users = users;
    this.clans = clans;
    this.tasks = tasks;
    this.userAddGoldService = userAddGoldService;
    this.deductGoldService = deductGoldService;
  }

  public void addClans() {
    for (int i = 1; i <= 20; i++) {
      int finalI = i;
      executor.submit(() -> {
        try {
          clans.addClan(
              Clan.builder()
                  .name("Clan_" + finalI)
                  .gold(1000)
                  .build());
          log.info("Add Clan: " + clans.getClan(finalI).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
    }
  }

  public void addUsers() {
    for (int i = 1; i <= 50; i++) {
      int finalI = i;
      executor.submit(() -> {
        try {
          users.addUser(
              User.builder()
                  .name("User_" + finalI)
                  .gold(1000)
                  .build());
          log.info("Add User: " + "User_" + finalI);
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
    }
  }

  public void addTasks() {
    for (int i = 1, j = 2; i <= 20; i++, j++) {
      int finalI = i;
      int finalJ = j;
      executor.submit(() -> {
        try {
          tasks.addTask(
              Task.builder()
                  .name("Task_" + finalI)
                  .gold(finalI * 5)
                  .progress(finalJ * 10)
                  .build());
          log.info("Add Task: " + "Task_" + finalI);
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
    }
  }

  public void addGoldToUser() {
    for (int i = 1, j = 50; i <= 50; i++, j--) {
      executorSubmit(i);
      executorSubmit(j);
    }
  }

  private void executorSubmit(int id) {
    executor.submit(() -> {
      try {
        int gold = 1000;
        users.changeUsersGold(id, gold);
        log.info("Add Gold: " + gold + " to User id " + id);
      } catch (SQLException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    });
  }

  public void addAndDeductGold() {

    for (int i = 1; i <= 300; i++) {

      executor.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(1, 1, 1);
          log.info("Add gold from User_1 to Clan_1");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(2, 2, 1);
          log.info("Add gold from User_2 to Clan_2");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(3, 3, 1);
          log.info("Add gold from User_3 to Clan_3");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(4, 4, 1);
          log.info("Add gold from User_4 to Clan_4");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(5, 5, 1);
          log.info("Add gold from User_5 to Clan_5");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(6, 6, 1);
          log.info("Add gold from User_6 to Clan_6");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(7, 6, 1);
          log.info("Add gold from User_7 to Clan_7");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(8, 6, 1);
          log.info("Add gold from User_8 to Clan_6");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(9, 6, 1);
          log.info("Add gold from User_9 to Clan_6");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(10, 6, 1);
          log.info("Add gold from User_10 to Clan_6");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });

      executor.submit(() -> {
        try {
          tasks.completeTask(6, 1);
          log.info("Complete Task_1");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(7, 2);
          log.info("Complete Task_2");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(8, 3);
          log.info("Complete Task_3");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(9, 4);
          log.info("Complete Task_4");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(10, 5);
          log.info("Complete Task_5");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(11, 6);
          log.info("Complete Task_6");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(12, 7);
          log.info("Complete Task_7");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(13, 8);
          log.info("Complete Task_8");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(14, 9);
          log.info("Complete Task_9");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(15, 10);
          log.info("Complete Task_10");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(16, 11);
          log.info("Complete Task_11");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(17, 12);
          log.info("Complete Task_12");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(18, 13);
          log.info("Complete Task_13");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(19, 14);
          log.info("Complete Task_14");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(20, 15);
          log.info("Complete Task_15");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(20, 16);
          log.info("Complete Task_16");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(20, 17);
          log.info("Complete Task_17");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(20, 18);
          log.info("Complete Task_18");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(20, 19);
          log.info("Complete Task_19");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(20, 20);
          log.info("Complete Task_20");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          deductGoldService.deductGoldFromClan(1, 1);
          log.info("Deduct gold from Clan_1");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          deductGoldService.deductGoldFromClan(2, 1);
          log.info("Deduct gold from Clan_2");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          deductGoldService.deductGoldFromClan(3, 1);
          log.info("Deduct gold from Clan_3");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          deductGoldService.deductGoldFromClan(4, 1);
          log.info("Deduct gold from Clan_4");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          deductGoldService.deductGoldFromClan(5, 1);
          log.info("Deduct gold from Clan_5");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          deductGoldService.deductGoldFromClan(6, 1);
          log.info("Deduct gold from Clan_6");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          deductGoldService.deductGoldFromClan(6, 1);
          log.info("Deduct gold from Clan_6");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          deductGoldService.deductGoldFromClan(6, 1);
          log.info("Deduct gold from Clan_6");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          deductGoldService.deductGoldFromClan(6, 1);
          log.info("Deduct gold from Clan_6");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          deductGoldService.deductGoldFromClan(6, 1);
          log.info("Deduct gold from Clan_6");
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });

    }
  }

}
