package com.codersergg;

import com.codersergg.db.ConnectionPool;
import com.codersergg.db.JdbcConnectionPool;
import com.codersergg.executor.Executor;
import com.codersergg.model.ApplicationLock;
import com.codersergg.repository.ClanRepository;
import com.codersergg.repository.UserRepository;
import com.codersergg.service.ClanService;
import com.codersergg.service.UserAddGoldService;
import com.codersergg.service.UserService;
import com.codersergg.utils.PopulateDB;
import com.codersergg.utils.TableManagementUtil;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import lombok.extern.java.Log;

@Log
public class Application {

  public static ConnectionPool connectionPool;

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

    } catch (SQLException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) throws InterruptedException, SQLException {
    ApplicationLock lock = new ApplicationLock();
    ClanService clans = new ClanRepository(connectionPool, lock);
    UserService users = new UserRepository(connectionPool, lock);
    UserAddGoldService userAddGoldService = new UserAddGoldService(lock, clans, users);
    ExecutorService executor = new Executor().getExecutorService();
    PopulateDB populateDB = new PopulateDB(executor, users, clans, userAddGoldService);

    populateDB.addClans();
    populateDB.addUsers();
    Thread.sleep(10000);
    populateDB.addGoldToUser();
    populateDB.updateGoldToUser();
    populateDB.addUsersGoldToClan();

  }

}