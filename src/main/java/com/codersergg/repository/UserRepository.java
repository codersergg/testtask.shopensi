package com.codersergg.repository;

import com.codersergg.db.ConnectionPool;
import com.codersergg.model.User;
import com.codersergg.service.UserService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.postgresql.util.PSQLException;

public class UserRepository implements UserService {

  private final ConnectionPool connectionPool;
  private volatile Connection connection;

  public UserRepository(ConnectionPool connectionPool) {
    this.connectionPool = connectionPool;
  }

  @Override
  public void addUser(User user) throws SQLException, InterruptedException {
    String query = "INSERT INTO user_player (name, gold, clanId) VALUES (?, ?, ?)";

    connection = connectionPool.getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, user.getName());
      preparedStatement.setInt(2, user.getGold());
      preparedStatement.setLong(3, user.getClanId());
      try {
        preparedStatement.execute();
        connectionPool.releaseConnection(connection);
      } catch (PSQLException e) {
        throw new RuntimeException(e);
      }
      try {
        getUser(user.getId());
      } catch (PSQLException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public Optional<User> getUser(long id) throws SQLException, InterruptedException {
    String query = "SELECT id, name, gold, clanId FROM user_player WHERE id = ?";

    connection = connectionPool.getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      ResultSet resultSet;
      preparedStatement.setLong(1, id);
      try {
        resultSet = preparedStatement.executeQuery();
        connectionPool.releaseConnection(connection);
        if (resultSet.next()) {
          User result = User.builder()
              .id(resultSet.getLong("id"))
              .name(resultSet.getString("name"))
              .gold(resultSet.getInt("gold"))
              .clanId(resultSet.getInt("clanId"))
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
}
