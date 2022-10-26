package com.codersergg.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.codersergg.ApplicationContext;
import com.codersergg.connection.JdbcConnectionPool;
import com.codersergg.executor.AppExecutor;
import com.codersergg.lock.LockServiceImpl;
import com.codersergg.model.Clan;
import com.codersergg.model.User;
import com.codersergg.monitoring.InMemoryMonitoring;
import com.codersergg.repository.ClanRepository;
import com.codersergg.repository.UserRepository;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import net.jodah.concurrentunit.Waiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserAddGoldServiceTest {

  private static final ExecutorService executorService = AppExecutor.getExecutorService();
  private final Waiter waiter = new Waiter();
  private ClanRepository clans;
  private UserRepository users;
  private UserAddGoldService userAddGoldService;

  @BeforeEach
  void initContext() throws SQLException, InterruptedException {
    LockServiceImpl lockService = new LockServiceImpl();
    JdbcConnectionPool connectionPool = JdbcConnectionPool
        .create("jdbc:h2:mem:test", "sergg", "pass");
    clans = new ClanRepository(connectionPool, lockService);
    users = new UserRepository(connectionPool, lockService);
    userAddGoldService = new UserAddGoldService(lockService, clans,
        new UserRepository(connectionPool, lockService),
        new InMemoryMonitoring(),
        AppExecutor.getExecutorService());
    ApplicationContext.initDB(connectionPool);
  }

  @Test
  void testAddGoldToClan() throws InterruptedException, SQLException {
    clans.addClan(Clan.builder().name("Clan_1").gold(0).build());
    users.addUser(User.builder().name("User_1").gold(100).build());
    userAddGoldService.addGoldToClan(1L, 1L, 100);
    assertEquals(100, clans.getClan(1L).orElseThrow().getGold());
    assertEquals(0, users.getUser(1L).orElseThrow().getGold());
  }

  @Test
  void testAddGoldToClanMultiThreads() throws InterruptedException, SQLException, TimeoutException {
    clans.addClan(Clan.builder().name("Clan_1").gold(0).build());
    users.addUser(User.builder().name("User_1").gold(100).build());
    users.addUser(User.builder().name("User_2").gold(100).build());
    Thread.sleep(50);

    for (int i = 0; i < 50; i++) {
      executorService.submit(() -> {
        try {
          userAddGoldService.addGoldToClan(1L, 1L, 1);
          userAddGoldService.addGoldToClan(2L, 1L, 1);
          waiter.resume();
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
    }
    waiter.await(10, TimeUnit.SECONDS, 50);
    assertEquals(100, clans.getClan(1L).orElseThrow().getGold());
    assertEquals(50, users.getUser(1L).orElseThrow().getGold());
    assertEquals(50, users.getUser(2L).orElseThrow().getGold());
  }

  @Test
  void testAddGoldToClanThrows() throws InterruptedException, SQLException {
    clans.addClan(Clan.builder().name("Clan_1").gold(0).build());
    users.addUser(User.builder().name("User_1").gold(100).build());

    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> userAddGoldService.addGoldToClan(1L, 1L, 101));
    String expected = "The user User(id=1, name=User_1, gold=100) does not have enough gold "
        + "for this action";
    assertEquals(expected, exception.getMessage());
  }
}
