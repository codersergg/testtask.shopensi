package com.codersergg;

import com.codersergg.db.ConnectionPool;
import com.codersergg.db.JdbcConnectionPool;
import com.codersergg.model.Clan;
import com.codersergg.model.User;
import com.codersergg.repository.ClanRepository;
import com.codersergg.repository.UserRepository;
import com.codersergg.service.ClanService;
import com.codersergg.service.UserService;
import com.codersergg.utils.TableManagementUtil;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.java.Log;

@Log
public class Application {

  public static ConnectionPool connectionPool;
  public static ExecutorService executor;

  static {
    try {
      connectionPool = JdbcConnectionPool
          .create("jdbc:postgresql://localhost:5432/postgres", "sergg", "pass");
      log.info("Created connection pool");
      TableManagementUtil util = new TableManagementUtil(connectionPool);
      util.dropTablesClan();
      log.info("Drop Table Clan");
      util.createTablesClan();
      log.info("Create Table Clan");
      util.dropTablesUser();
      log.info("Drop Table User");
      util.createTablesUser();
      log.info("Create Table User");
      executor = Executors.newFixedThreadPool(20);
    } catch (SQLException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) {

    ClanService clanService = new ClanRepository(connectionPool);
    saveClans(clanService);

    UserService userService = new UserRepository(connectionPool);
    saveUsers(userService);
  }

  private static void saveClans(ClanService clanService) {
    for (int i = 1; i <= 10; i++) {
      int finalI = i;
      executor.submit(() -> {
        try {
          clanService.addClan(
              Clan.builder()
                  .name("Clan_" + finalI)
                  .gold(0)
                  .build());
          log.info("Set Clan: " + clanService.getClan(finalI).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
    }
  }

  private static void saveUsers(UserService userService) {
    for (int i = 1, j = 100; i <= 1000; i++, j += 1) {
      int finalI = i;
      int finalJ = j;
      executor.submit(() -> {
        try {
          userService.addUser(
              User.builder()
                  .name("User_" + finalI)
                  .gold(0)
                  .clanId(finalJ / 100)
                  .build());
          log.info("Set User: " + userService.getUser(finalI).orElseThrow());
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
    }
  }
}