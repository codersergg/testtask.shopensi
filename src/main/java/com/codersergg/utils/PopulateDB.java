package com.codersergg.utils;

import com.codersergg.model.Clan;
import com.codersergg.model.Task;
import com.codersergg.model.User;
import com.codersergg.service.ClanService;
import com.codersergg.service.TaskService;
import com.codersergg.service.UserAddGoldService;
import com.codersergg.service.UserService;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import lombok.extern.java.Log;

@Log
public class PopulateDB {

  private final ExecutorService executor;
  private final UserService users;
  private final ClanService clans;
  private final TaskService tasks;
  private final UserAddGoldService userAddGoldService;

  public PopulateDB(ExecutorService executor, UserService users, ClanService clans,
      TaskService tasks, UserAddGoldService userAddGoldService) {
    this.executor = executor;
    this.users = users;
    this.clans = clans;
    this.tasks = tasks;
    this.userAddGoldService = userAddGoldService;
  }

  public void addClans() {
    for (int i = 1; i <= 10; i++) {
      int finalI = i;
      executor.submit(() -> {
        try {
          clans.addClan(
              Clan.builder()
                  .name("Clan_" + finalI)
                  .gold(0)
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
                  .gold(0)
                  .build());
          log.info("Add User: " + users.getUser(finalI).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
    }
  }

  public void addTasks() {
    for (int i = 1, j = 2; i <= 5; i++, j++) {
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
          log.info("Add Task: " + tasks.getTask(finalI).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
    }
  }

  public void addGoldToUser() throws SQLException, InterruptedException {
    for (int i = 1, j = 50; i <= 50; i++, j--) {
      executorSubmit(i, users.getUser(i));
      executorSubmit(j, users.getUser(j));
    }
  }

  private void executorSubmit(int id, Optional<User> userOptional) {
    executor.submit(() -> {
      try {
        users.populateUsersGold(
            User.builder()
                .id(id)
                .name(userOptional.orElseThrow().getName())
                .gold(500)
                .build());
        log.info("Add Gold to User: " + userOptional.orElseThrow());
      } catch (SQLException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    });
  }

  public void addGoldToClan() {

    for (int i = 1; i <= 400; i++) {
      executor.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(1, 1, 1);
          log.info("Add User: " + users.getUser(1).orElseThrow() +
              " to Clan: " + clans.getClan(1).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(2, 1, 1);
          log.info("Add User: " + users.getUser(2).orElseThrow() +
              " to Clan: " + clans.getClan(1).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(3, 1, 1);
          log.info("Add User: " + users.getUser(3).orElseThrow() +
              " to Clan: " + clans.getClan(1).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(4, 1, 1);
          log.info("Add User: " + users.getUser(4).orElseThrow() +
              " to Clan: " + clans.getClan(1).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(5, 2, 1);
          log.info("Add User: " + users.getUser(5).orElseThrow() +
              " to Clan: " + clans.getClan(2).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(6, 3, 1);
          log.info("Add User: " + users.getUser(6).orElseThrow() +
              " to Clan: " + clans.getClan(3).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(7, 4, 1);
          log.info("Add User: " + users.getUser(7).orElseThrow() +
              " to Clan: " + clans.getClan(4).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(8, 5, 1);
          log.info("Add User: " + users.getUser(8).orElseThrow() +
              " to Clan: " + clans.getClan(5).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(9, 6, 1);
          log.info("Add User: " + users.getUser(9).orElseThrow() +
              " to Clan: " + clans.getClan(6).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(10, 7, 2);
          log.info("Add User: " + users.getUser(10).orElseThrow() +
              " to Clan: " + clans.getClan(2).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(7, 1);
          log.info("Complete Task: " + tasks.getTask(1).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(8, 2);
          log.info("Complete Task: " + tasks.getTask(1).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(9, 3);
          log.info("Complete Task: " + tasks.getTask(1).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(10, 4);
          log.info("Complete Task: " + tasks.getTask(1).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          tasks.completeTask(9, 5);
          log.info("Complete Task: " + tasks.getTask(1).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
    }
  }

}
