package com.codersergg.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.codersergg.ApplicationContext;
import com.codersergg.connection.JdbcConnectionPool;
import com.codersergg.executor.AppExecutor;
import com.codersergg.lock.LockServiceImpl;
import com.codersergg.model.Clan;
import com.codersergg.monitoring.InMemoryMonitoring;
import com.codersergg.repository.ClanRepository;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import net.jodah.concurrentunit.Waiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeductGoldServiceTest {

  private static final ExecutorService executorService = AppExecutor.getExecutorService();
  private final Waiter waiter = new Waiter();
  private ClanRepository clans;
  private DeductGoldService deductGoldService;

  @BeforeEach
  void initContext() throws SQLException, InterruptedException {
    LockServiceImpl lockService = new LockServiceImpl();
    JdbcConnectionPool connectionPool = JdbcConnectionPool
        .create("jdbc:h2:mem:test", "sergg", "pass");
    clans = new ClanRepository(connectionPool, lockService);
    deductGoldService = new DeductGoldService(lockService, clans,
        new InMemoryMonitoring(),
        AppExecutor.getExecutorService());
    ApplicationContext.initDB(connectionPool);
  }

  @Test
  void testDeductGoldFromClan() throws InterruptedException, SQLException {
    clans.addClan(Clan.builder().name("Clan_1").gold(11).build());
    deductGoldService.deductGoldFromClan(1L, 10);
    assertEquals(1, clans.getClan(1L).orElseThrow().getGold());
  }

  @Test
  void testDeductGoldFromClanMultiThreads()
      throws InterruptedException, SQLException, TimeoutException {
    clans.addClan(Clan.builder().name("Clan_1").gold(101).build());

    for (int i = 0; i < 50; i++) {
      executorService.submit(() -> {
        try {
          deductGoldService.deductGoldFromClan(1L, 2);
          waiter.resume();
        } catch (SQLException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
    }
    waiter.await(10, TimeUnit.SECONDS, 50);
    assertEquals(1, clans.getClan(1L).orElseThrow().getGold());
  }

  @Test
  void testDeductGoldFromClanThrows() throws InterruptedException, SQLException {
    clans.addClan(Clan.builder().name("Clan_1").gold(1).build());
    assertEquals(1, clans.getClan(1L).orElseThrow().getGold());

    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> deductGoldService.deductGoldFromClan(1L, 10));
    String expected = "The clan Clan(id=1, name=Clan_1, gold=1) does not have enough gold for this action";
    assertEquals(expected, exception.getMessage());
  }

  @Test
  void testDeductGoldFromClanThrows2() throws InterruptedException, SQLException {
    clans.addClan(Clan.builder().name("Clan_1").gold(1).build());
    assertEquals(1, clans.getClan(1L).orElseThrow().getGold());

    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> deductGoldService.deductGoldFromClan(2L, 10));
    String expected = "No value present";
    assertEquals(expected, exception.getMessage());
  }
}
