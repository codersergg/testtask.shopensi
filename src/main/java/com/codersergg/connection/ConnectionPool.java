package com.codersergg.connection;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionPool {

  Connection getConnection() throws InterruptedException, SQLException;

  boolean releaseConnection(Connection connection);

  String getUrl();

  String getUser();

  String getPassword();
}