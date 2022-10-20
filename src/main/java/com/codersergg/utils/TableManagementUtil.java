package com.codersergg.utils;

import com.codersergg.db.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lombok.extern.java.Log;

@Log
public class TableManagementUtil {

  private final ConnectionPool connectionPool;
  private Connection connection;

  public TableManagementUtil(ConnectionPool connectionPool) {
    this.connectionPool = connectionPool;
  }

  public void createTablesClan() throws SQLException, InterruptedException {
    String query = "CREATE TABLE clan " +
        "(id BIGSERIAL primary key, " +
        "name VARCHAR (20) NOT NULL, " +
        "gold int4, " +
        "CONSTRAINT clan_constraint UNIQUE (id, name))";
    connection = connectionPool.getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.execute();
      connectionPool.releaseConnection(connection);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void dropTablesClan() throws SQLException, InterruptedException {
    String query = "DROP TABLE IF EXISTS clan";
    connection = connectionPool.getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.execute();
      connectionPool.releaseConnection(connection);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void createTablesUser() throws SQLException, InterruptedException {
    String query = "CREATE TABLE user_player " +
        "(id BIGSERIAL primary key, " +
        "name VARCHAR (20) NOT NULL, " +
        "gold int4, " +
        "clanId int8, " +
        "CONSTRAINT user_constraint UNIQUE (id, name))";
    connection = connectionPool.getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.execute();
      connectionPool.releaseConnection(connection);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void dropTablesUser() throws SQLException, InterruptedException {
    String query = "DROP TABLE IF EXISTS user_player";
    connection = connectionPool.getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.execute();
      connectionPool.releaseConnection(connection);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}