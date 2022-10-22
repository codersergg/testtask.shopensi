package com.codersergg;

import com.codersergg.db.ConnectionPool;
import com.codersergg.db.JdbcConnectionPool;
import com.codersergg.executor.Executor;
import com.codersergg.lock.LockService;
import com.codersergg.lock.LockServiceImpl;
import com.codersergg.monitoring.InMemoryMonitoring;
import com.codersergg.monitoring.Monitoring;
import com.codersergg.repository.ClanRepository;
import com.codersergg.repository.TaskRepository;
import com.codersergg.repository.UserRepository;
import com.codersergg.service.ClanService;
import com.codersergg.service.DeductGoldService;
import com.codersergg.service.TaskService;
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
      util.dropTablesTask();
      log.info("Drop Table Task");
      util.createTablesTask();
      log.info("Create Table Task");

    } catch (SQLException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) throws InterruptedException {
    LockService lock = new LockServiceImpl();
    UserService users = new UserRepository(connectionPool, lock);
    ClanService clans = new ClanRepository(connectionPool, lock);
    TaskRepository taskRepository = new TaskRepository(connectionPool, lock);

    Monitoring monitoring = new InMemoryMonitoring();

    TaskService tasks = new TaskService(lock, clans, taskRepository, monitoring);
    UserAddGoldService userAddGoldService = new UserAddGoldService(lock, clans, users, monitoring);
    DeductGoldService deductGoldService = new DeductGoldService(lock, clans, monitoring);

    ExecutorService executor = new Executor().getExecutorService();
    PopulateDB populateDB = new PopulateDB(executor, users, clans, tasks, userAddGoldService,
        deductGoldService);

    // Инициализация БД
    populateDB.addClans();
    populateDB.addUsers();
    populateDB.addTasks();

    // Запуск имитации действий пользователей
    populateDB.addGoldToUser();
    populateDB.addAndDeductGold();

    Thread.sleep(4 * 60 * 1000);
    monitoring.getMetricMap().forEach(
        (id, metric) -> System.out.println("id metric: " + id + " metric: " + metric.toString()));

  }

}