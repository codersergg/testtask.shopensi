package com.codersergg.repository;

import com.codersergg.connection.ConnectionPool;
import com.codersergg.lock.LockService;
import com.codersergg.model.Task;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.postgresql.util.PSQLException;

public class TaskRepository {

  private final ConnectionPool connectionPool;
  private final LockService lockService;
  private volatile Connection connection;

  public TaskRepository(ConnectionPool connectionPool, LockService lockService) {
    this.connectionPool = connectionPool;
    this.lockService = lockService;
  }

  public Optional<Task> getTask(long taskId) throws SQLException, InterruptedException {
    String query = "SELECT id, name, gold, progress FROM task WHERE id = ?";
    connection = connectionPool.getConnection();

    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      ResultSet resultSet;
      preparedStatement.setLong(1, taskId);
      try {
        resultSet = preparedStatement.executeQuery();
        connectionPool.releaseConnection(connection);
        if (resultSet.next()) {
          Task result = Task.builder()
              .id(resultSet.getLong("id"))
              .name(resultSet.getString("name"))
              .gold(resultSet.getInt("gold"))
              .progress(resultSet.getInt("progress"))
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

  public void addTask(Task task) throws SQLException, InterruptedException {
    String query = "INSERT INTO task (name, gold, progress) VALUES (?, ?, ?)";
    connection = connectionPool.getConnection();

    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, task.getName());
      preparedStatement.setInt(2, task.getGold());
      preparedStatement.setInt(3, task.getProgress());
      try {
        preparedStatement.execute();
        connectionPool.releaseConnection(connection);
      } catch (PSQLException e) {
        throw new SQLException(e);
      }
    }
  }

  public void deductTaskProgress(long taskId) throws SQLException, InterruptedException {
    synchronized (lockService.getLock(getTask(taskId).orElseThrow())) {
      Task task = getTask(taskId).orElseThrow();
      updateProgress(taskId, task.getProgress() - 1);
    }
  }

  private void updateProgress(long taskId, int newProgress)
      throws InterruptedException, SQLException {
    connection = connectionPool.getConnection();
    String query = "UPDATE task SET progress = ? WHERE id = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, newProgress);
      preparedStatement.setLong(2, taskId);
      try {
        preparedStatement.executeUpdate();
        connectionPool.releaseConnection(connection);
      } catch (PSQLException e) {
        throw new SQLException(e);
      }
    }
  }
}
