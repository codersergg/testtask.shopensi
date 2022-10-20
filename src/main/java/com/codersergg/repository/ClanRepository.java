package com.codersergg.repository;

import com.codersergg.db.ConnectionPool;
import com.codersergg.model.Clan;
import com.codersergg.service.ClanService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.postgresql.util.PSQLException;

public class ClanRepository implements ClanService {

  private final ConnectionPool connectionPool;

  public ClanRepository(ConnectionPool connectionPool) {
    this.connectionPool = connectionPool;
  }

  @Override
  public Optional<Clan> getClan(long id) throws SQLException, InterruptedException {
    String query = "SELECT id, name, gold FROM clan WHERE id = ?";

    Connection connection = connectionPool.getConnection();
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
        throw new RuntimeException(e);
      }
    }
  }

  public void addClan(Clan clan) throws SQLException, InterruptedException {
    String query = "INSERT INTO clan (name, gold) VALUES (?, ?)";

    Connection connection = connectionPool.getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, clan.getName());
      preparedStatement.setInt(2, clan.getGold());
      try {
        preparedStatement.execute();
        connectionPool.releaseConnection(connection);
      } catch (PSQLException e) {
        throw new RuntimeException(e);
      }
      try {
        getClan(clan.getId());
      } catch (PSQLException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public void updateClan(Clan clan) throws SQLException, InterruptedException {
    String query = "INSERT INTO clan (id, name, gold) VALUES (?, ?, ?)";

    Connection connection = connectionPool.getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setLong(1, clan.getId());
      preparedStatement.setString(2, clan.getName());
      preparedStatement.setInt(3, clan.getGold());
      try {
        preparedStatement.execute();
        connectionPool.releaseConnection(connection);
      } catch (PSQLException e) {
        throw new RuntimeException(e);
      }
      try {
        getClan(clan.getId());
      } catch (PSQLException e) {
        throw new RuntimeException(e);
      }
    }
  }

}
