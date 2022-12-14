package com.codersergg.repository;

import com.codersergg.connection.ConnectionPool;
import com.codersergg.lock.LockService;
import com.codersergg.model.Clan;
import com.codersergg.monitoring.model.GoldValues;
import com.codersergg.service.ClanService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import lombok.extern.java.Log;
import org.postgresql.util.PSQLException;

@Log
public class ClanRepository implements ClanService {

  private final ConnectionPool connectionPool;
  private final LockService lockService;
  private volatile Connection connection;

  public ClanRepository(ConnectionPool connectionPool, LockService lockService) {
    this.connectionPool = connectionPool;
    this.lockService = lockService;
  }

  @Override
  public Optional<Clan> getClan(long id) throws SQLException, InterruptedException {
    String query = "SELECT id, name, gold FROM clan WHERE id = ?";
    connection = connectionPool.getConnection();

    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      ResultSet resultSet;
      preparedStatement.setLong(1, id);
      try {
        resultSet = preparedStatement.executeQuery();
        connectionPool.releaseConnection(connection);
        if (resultSet.next()) {
          Clan result = Clan.builder()
              .id(resultSet.getLong("id"))
              .name(resultSet.getString("name"))
              .gold(resultSet.getInt("gold"))
              .build();

          return Optional.of(result);
        } else {
          return Optional.empty();
        }
      } catch (PSQLException e) {
        throw new SQLException(e);
      }
    }
  }

  public void addClan(Clan clan) throws SQLException, InterruptedException {
    String query = "INSERT INTO clan (name, gold) VALUES (?, ?)";
    connection = connectionPool.getConnection();

    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, clan.getName());
      preparedStatement.setInt(2, clan.getGold());
      try {
        preparedStatement.execute();
        connectionPool.releaseConnection(connection);
      } catch (PSQLException e) {
        throw new SQLException(e);
      }
    }
  }

  /**
   * ?????????? ?????????????????? ???????????????????? ???????????? ??????????
   *
   * @param clanId
   * @param gold   ???????????????????? ????????????, ?????????????????????? ???????????????????? ??????????
   * @throws SQLException
   * @throws InterruptedException
   */
  @Override
  public GoldValues changeClansGold(long clanId, int gold)
      throws SQLException, InterruptedException {
    Lock lock = lockService.getLock(getClan(clanId).orElseThrow());
    lock.lock();
    try {
      Clan clan = getClan(clanId).orElseThrow();
      goldSufficiencyCheck(clan, clan.getGold() + gold);
      updateGold(clan, clan.getGold() + gold);

      return GoldValues.builder()
          .dateTime(LocalDateTime.now())
          .amountGoldBeforeRaise(clan.getGold())
          .amountGoldToRaise(gold)
          .amountGoldAfterRaise(clan.getGold() + gold)
          .build();
    } finally {
      lock.unlock();
    }
  }

  private void goldSufficiencyCheck(Clan clan, int newGold) {
    if (newGold < 0) {
      throw new IllegalStateException(
          String.format("The clan %s does not have enough gold for this action", clan));
    }
  }

  public void updateGold(Clan clan, int newGold) throws SQLException, InterruptedException {
    connection = connectionPool.getConnection();
    String query = "UPDATE clan SET gold = ? WHERE id = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, newGold);
      preparedStatement.setLong(2, clan.getId());
      try {
        preparedStatement.executeUpdate();
        connectionPool.releaseConnection(connection);
      } catch (PSQLException e) {
        throw new SQLException(e);
      }
    }
  }

}
