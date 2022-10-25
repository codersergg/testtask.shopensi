package com.codersergg.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.codersergg.ApplicationContext;
import com.codersergg.connection.JdbcConnectionPool;
import com.codersergg.executor.AppExecutor;
import com.codersergg.lock.LockServiceImpl;
import com.codersergg.model.Clan;
import com.codersergg.model.Task;
import com.codersergg.monitoring.InMemoryMonitoring;
import com.codersergg.repository.ClanRepository;
import com.codersergg.repository.TaskRepository;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import net.jodah.concurrentunit.Waiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskServiceTest {

  private final Waiter waiter = new Waiter();
  private final ExecutorService executorService = AppExecutor.getExecutorService();
  private ClanRepository clans;
  private TaskService taskService;

  @BeforeEach
  void initContext() throws SQLException, InterruptedException {
    LockServiceImpl lockService = new LockServiceImpl();
    JdbcConnectionPool connectionPool = JdbcConnectionPool
        .create("jdbc:h2:mem:test", "sergg", "pass");
    clans = new ClanRepository(connectionPool, lockService);
    taskService = new TaskService(lockService, clans,
        new TaskRepository(connectionPool, lockService),
        new InMemoryMonitoring(),
        AppExecutor.getExecutorService());
    ApplicationContext.initDB(connectionPool);
  }

  @Test
  void testCompleteTask() throws InterruptedException, SQLException {
    clans.addClan(Clan.builder().name("Clan_1").gold(0).build());
    taskService.addTask(Task.builder()
        .name("Task_1")
        .gold(50)
        .progress(1)
        .build());
    taskService.completeTask(1L, 1L);

    assertEquals(50, clans.getClan(1L).orElseThrow().getGold());
  }

  @Test
  void testCompleteTaskMultiThreads() throws InterruptedException, SQLException, TimeoutException {
    clans.addClan(Clan.builder().name("Clan_1").gold(0).build());
    taskService.addTask(Task.builder()
        .name("Task_1")
        .gold(50)
        .progress(50)
        .build());

    for (int i = 0; i < 50; i++) {
      executorService.submit(() -> {
        try {
          taskService.completeTask(1L, 1L);
          waiter.resume();
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
    }
    waiter.await(20, TimeUnit.SECONDS, 50);
    assertEquals(50, clans.getClan(1L).orElseThrow().getGold());
  }

  @Test
  void testCompleteTaskNoThrows() throws InterruptedException, SQLException, TimeoutException {
    clans.addClan(Clan.builder().name("Clan_1").gold(0).build());
    taskService.addTask(Task.builder()
        .name("Task_1")
        .gold(50)
        .progress(50)
        .build());
    assertDoesNotThrow(() -> {
      for (int i = 0; i < 51; i++) {
        executorService.submit(() -> {
          try {
            taskService.completeTask(1L, 1L);
            waiter.resume();
          } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
          }
        });
      }
    });
    waiter.await(20, TimeUnit.SECONDS, 50);
    assertEquals(50, clans.getClan(1L).orElseThrow().getGold());
    assertEquals(50, taskService.getTask(1L).orElseThrow().getGold());
    assertEquals(0, taskService.getTask(1L).orElseThrow().getProgress());
  }
}
