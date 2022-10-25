package com.codersergg;

import com.codersergg.connection.ConnectionPool;
import com.codersergg.connection.JdbcConnectionPool;
import com.codersergg.executor.AppExecutor;
import com.codersergg.lock.LockService;
import com.codersergg.lock.LockServiceImpl;
import com.codersergg.monitoring.InMemoryMonitoring;
import com.codersergg.monitoring.KafkaMonitoring;
import com.codersergg.monitoring.Monitoring;
import com.codersergg.monitoring.producer.MonitoringKafkaProducer;
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
public class ApplicationContext {

  public static ConnectionPool connectionPool;

  public static void init() {
    LockService lock = new LockServiceImpl();
    UserService users = new UserRepository(connectionPool, lock);
    ClanService clans = new ClanRepository(connectionPool, lock);
    TaskRepository taskRepository = new TaskRepository(connectionPool, lock);
    ExecutorService executor = AppExecutor.getExecutorService();
    ExecutorService kafkaExecutor = AppExecutor.getKafkaExecutorService();
    Monitoring monitoring = new KafkaMonitoring(
        new MonitoringKafkaProducer(new InMemoryMonitoring()));
    TaskService tasks = new TaskService(lock, clans, taskRepository, monitoring, kafkaExecutor);
    UserAddGoldService userAddGoldService = new UserAddGoldService(lock, clans, users, monitoring,
        kafkaExecutor);
    DeductGoldService deductGoldService = new DeductGoldService(lock, clans, monitoring,
        kafkaExecutor);
    PopulateDB populateDB = new PopulateDB(executor, users, clans, tasks, userAddGoldService,
        deductGoldService);
    initPopulateDB(populateDB);
    initWorkTask(populateDB);
  }

  public static ConnectionPool initConnectionPool(String url, String user, String password)
      throws SQLException {
    connectionPool = JdbcConnectionPool.create(url, user, password);
    return connectionPool;
  }

  public static void initDB(ConnectionPool connectionPool)
      throws SQLException, InterruptedException {
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
  }

  public static void initPopulateDB(PopulateDB populateDB) {
    // Популяция БД
    populateDB.addClans();
    populateDB.addUsers();
    populateDB.addTasks();
  }

  public static void initWorkTask(PopulateDB populateDB) {
    // Запуск имитации действий пользователей
    populateDB.addGoldToUser();
    populateDB.addAndDeductGold();
  }

}
