package com.codersergg.repository;

import com.codersergg.db.ConnectionPool;
import com.codersergg.model.ApplicationLock;
import com.codersergg.model.User;
import com.codersergg.service.UserService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import lombok.extern.java.Log;
import org.postgresql.util.PSQLException;

@Log
public class UserRepository implements UserService {

  private final ConnectionPool connectionPool;
  private final ApplicationLock applicationLock;
  private volatile Connection connection;

  public UserRepository(ConnectionPool connectionPool, ApplicationLock applicationLock) {
    this.connectionPool = connectionPool;
    this.applicationLock = applicationLock;
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

  /**
   * Метод изменения количества золота пользователя
   *
   * @param userId
   * @param gold количество золота, подлежащего внесению в БД
   * @throws SQLException
   * @throws InterruptedException
   */
  @Override
  public void changeUsersGold(long userId, int gold) throws SQLException, InterruptedException {
    synchronized (applicationLock.getLock(getUser(userId).orElseThrow())) {
      User user = getUser(userId).orElseThrow();
      goldSufficiencyCheck(user, gold);
      updateGold(user, gold);
    }
  }

  /**
   * Метод для популяции БД
   *
   * @param userGoldUpdate Объект содержащий количество золота подлежащего добавлению (если поде
   *                       gold положительное) или уменишению (если поде gold отрицательное).
   * @throws SQLException
   * @throws InterruptedException
   */
  @Override
  public void populateUsersGold(User userGoldUpdate) throws SQLException, InterruptedException {
    synchronized (applicationLock.getLock(userGoldUpdate)) {
      User user = getUser(userGoldUpdate.getId()).orElseThrow();
      int newGold = user.getGold() + userGoldUpdate.getGold();
      goldSufficiencyCheck(user, newGold);
      updateGold(user, newGold);
    }
  }

  private void goldSufficiencyCheck(User user, int newGold) {
    if (newGold < 0) {
      throw new IllegalStateException(
          String.format("The user %s does not have enough gold for this action", user));
    }
  }

  private void updateGold(User user, int newGold) throws InterruptedException, SQLException {
    connection = connectionPool.getConnection();
    String query = "UPDATE user_player SET gold = ? WHERE id = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, newGold);
      preparedStatement.setLong(2, user.getId());
      try {
        preparedStatement.executeUpdate();
        connectionPool.releaseConnection(connection);
      } catch (PSQLException e) {
        throw new RuntimeException(e);
      }
    }
  }
}