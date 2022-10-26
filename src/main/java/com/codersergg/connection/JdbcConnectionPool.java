package com.codersergg.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.Getter;

@Getter
public class JdbcConnectionPool implements ConnectionPool {

  private static final int INITIAL_POOL_SIZE = 20;
  private static final int MAX_POOL_SIZE = 40;
  private static final int MAX_TIMEOUT = 5;
  private final String url;
  private final String user;
  private final String password;
  private final Queue<Connection> connectionPool;
  private final Queue<Connection> usedConnections = new ConcurrentLinkedQueue<>();

  public JdbcConnectionPool(String url, String user, String password,
      Queue<Connection> connectionPool) {
    this.url = url;
    this.user = user;
    this.password = password;
    this.connectionPool = connectionPool;
  }

  public static JdbcConnectionPool create(String url, String user, String password)
      throws SQLException {
    Queue<Connection> pool = new ConcurrentLinkedQueue<>();
    for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
      pool.add(createConnection(url, user, password));
    }
    return new JdbcConnectionPool(url, user, password, pool);
  }

  @Override
  public Connection getConnection() throws SQLException {

    if (connectionPool.isEmpty()) {
      if (usedConnections.size() < MAX_POOL_SIZE) {
        connectionPool.add(createConnection(url, user, password));
      } else {
        throw new RuntimeException("Maximum pool size reached, no available connections!");
      }
    }

    Connection connection = connectionPool.poll();
    assert connection != null;
    if (!connection.isValid(MAX_TIMEOUT)) {
      connection = createConnection(url, user, password);
    }
    usedConnections.add(connection);

    return connection;
  }

  @Override
  public boolean releaseConnection(Connection connection) {
    connectionPool.add(connection);
    return usedConnections.remove(connection);
  }

  private static Connection createConnection(String url, String user, String password)
      throws SQLException {
    return DriverManager.getConnection(url, user, password);
  }

  public int getSize() {
    return connectionPool.size() + usedConnections.size();
  }
}