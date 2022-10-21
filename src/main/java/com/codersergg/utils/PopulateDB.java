package com.codersergg.utils;

import com.codersergg.model.Clan;
import com.codersergg.model.User;
import com.codersergg.service.ClanService;
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

  private final UserAddGoldService userAddGoldService;

  public PopulateDB(ExecutorService executor, UserService users, ClanService clans,
      UserAddGoldService userAddGoldService) {
    this.executor = executor;
    this.users = users;
    this.clans = clans;
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
    for (int i = 1, j = 100; i <= 1000; i++, j += 1) {
      int finalI = i;
      int finalJ = j;
      executor.submit(() -> {
        try {
          users.addUser(
              User.builder()
                  .name("User_" + finalI)
                  .gold(0)
                  .clanId(finalJ / 100)
                  .build());
          log.info("Add User: " + users.getUser(finalI).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
    }
  }

  public void addGoldToUser() throws SQLException, InterruptedException {
    for (int i = 1, j = 1000; i <= 1000; i++, j--) {
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
                .gold(10)
                .build());
        log.info("Add Gold to User: " + userOptional.orElseThrow());
      } catch (SQLException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    });
  }

  public void updateGoldToUser() {
    for (int i = 0; i < 2000; i++) {
      executor.submit(() -> {
        try {
          Optional<User> user = users.getUser(1);
          User userGoldUpdate = user.orElseThrow();
          userGoldUpdate.setGold(2);
          users.populateUsersGold(userGoldUpdate);
          log.info("Add Gold to User: " + user.orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          Optional<User> user = users.getUser(2);
          User userGoldUpdate = user.orElseThrow();
          userGoldUpdate.setGold(2);
          users.populateUsersGold(userGoldUpdate);
          log.info("Add Gold to User: " + user.orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          Optional<User> user = users.getUser(3);
          User userGoldUpdate = user.orElseThrow();
          userGoldUpdate.setGold(2);
          users.populateUsersGold(userGoldUpdate);
          log.info("Add Gold to User: " + user.orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          Optional<User> user = users.getUser(4);
          User userGoldUpdate = user.orElseThrow();
          userGoldUpdate.setGold(2);
          users.populateUsersGold(userGoldUpdate);
          log.info("Add Gold to User: " + user.orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          Optional<User> user = users.getUser(5);
          User userGoldUpdate = user.orElseThrow();
          userGoldUpdate.setGold(5);
          users.populateUsersGold(userGoldUpdate);
          log.info("Add Gold to User: " + user.orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          Optional<User> user = users.getUser(6);
          User userGoldUpdate = user.orElseThrow();
          userGoldUpdate.setGold(2);
          users.populateUsersGold(userGoldUpdate);
          log.info("Add Gold to User: " + user.orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          Optional<User> user = users.getUser(7);
          User userGoldUpdate = user.orElseThrow();
          userGoldUpdate.setGold(2);
          users.populateUsersGold(userGoldUpdate);
          log.info("Add Gold to User: " + user.orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          Optional<User> user = users.getUser(8);
          User userGoldUpdate = user.orElseThrow();
          userGoldUpdate.setGold(2);
          users.populateUsersGold(userGoldUpdate);
          log.info("Add Gold to User: " + user.orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          Optional<User> user = users.getUser(9);
          User userGoldUpdate = user.orElseThrow();
          userGoldUpdate.setGold(2);
          users.populateUsersGold(userGoldUpdate);
          log.info("Add Gold to User: " + user.orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          Optional<User> user = users.getUser(10);
          User userGoldUpdate = user.orElseThrow();
          userGoldUpdate.setGold(4);
          users.populateUsersGold(userGoldUpdate);
          log.info("Add Gold to User: " + user.orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          Optional<User> user = users.getUser(1);
          User userGoldUpdate = user.orElseThrow();
          userGoldUpdate.setGold(-1);
          users.populateUsersGold(userGoldUpdate);
          log.info("Add Gold to User: " + user.orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          Optional<User> user = users.getUser(2);
          User userGoldUpdate = user.orElseThrow();
          userGoldUpdate.setGold(-1);
          users.populateUsersGold(userGoldUpdate);
          log.info("Add Gold to User: " + user.orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          Optional<User> user = users.getUser(3);
          User userGoldUpdate = user.orElseThrow();
          userGoldUpdate.setGold(-1);
          users.populateUsersGold(userGoldUpdate);
          log.info("Add Gold to User: " + user.orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          Optional<User> user = users.getUser(4);
          User userGoldUpdate = user.orElseThrow();
          userGoldUpdate.setGold(-1);
          users.populateUsersGold(userGoldUpdate);
          log.info("Add Gold to User: " + user.orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      executor.submit(() -> {
        try {
          Optional<User> user = users.getUser(5);
          User userGoldUpdate = user.orElseThrow();
          userGoldUpdate.setGold(-1);
          users.populateUsersGold(userGoldUpdate);
          log.info("Add Gold to User: " + user.orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });executor.submit(() -> {
        try {
          Optional<User> user = users.getUser(6);
          User userGoldUpdate = user.orElseThrow();
          userGoldUpdate.setGold(-1);
          users.populateUsersGold(userGoldUpdate);
          log.info("Add Gold to User: " + user.orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });executor.submit(() -> {
        try {
          Optional<User> user = users.getUser(7);
          User userGoldUpdate = user.orElseThrow();
          userGoldUpdate.setGold(-1);
          users.populateUsersGold(userGoldUpdate);
          log.info("Add Gold to User: " + user.orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });executor.submit(() -> {
        try {
          Optional<User> user = users.getUser(8);
          User userGoldUpdate = user.orElseThrow();
          userGoldUpdate.setGold(-1);
          users.populateUsersGold(userGoldUpdate);
          log.info("Add Gold to User: " + user.orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });executor.submit(() -> {
        try {
          Optional<User> user = users.getUser(9);
          User userGoldUpdate = user.orElseThrow();
          userGoldUpdate.setGold(-1);
          users.populateUsersGold(userGoldUpdate);
          log.info("Add Gold to User: " + user.orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });executor.submit(() -> {
        try {
          Optional<User> user = users.getUser(10);
          User userGoldUpdate = user.orElseThrow();
          userGoldUpdate.setGold(-1);
          users.populateUsersGold(userGoldUpdate);
          log.info("Add Gold to User: " + user.orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
    }
  }

  public void addUsersGoldToClan() {

    for (int i = 1; i <= 2000; i++) {
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
      });executor.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(6, 3, 1);
          log.info("Add User: " + users.getUser(6).orElseThrow() +
              " to Clan: " + clans.getClan(3).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });executor.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(7, 4, 1);
          log.info("Add User: " + users.getUser(7).orElseThrow() +
              " to Clan: " + clans.getClan(4).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });executor.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(8, 5, 1);
          log.info("Add User: " + users.getUser(8).orElseThrow() +
              " to Clan: " + clans.getClan(5).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });executor.submit(() -> {
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
          userAddGoldService.addGoldToClan(10, 2, 2);
          log.info("Add User: " + users.getUser(10).orElseThrow() +
              " to Clan: " + clans.getClan(2).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
    }
  }

}
