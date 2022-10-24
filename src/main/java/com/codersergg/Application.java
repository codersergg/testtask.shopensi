package com.codersergg;

import java.sql.SQLException;
import lombok.extern.java.Log;

@Log
public class Application {

  public static void main(String[] args) throws SQLException, InterruptedException {
    ApplicationContext.initDB(ApplicationContext.initConnectionPool(
        "jdbc:postgresql://localhost:5432/postgres",
        "sergg",
        "pass"));
    ApplicationContext.init();
  }

}