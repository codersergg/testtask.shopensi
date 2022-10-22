package com.codersergg.service;

import com.codersergg.model.User;
import java.sql.SQLException;
import java.util.Optional;

public interface UserService {

  Optional<User> getUser(long userId) throws SQLException, InterruptedException;

  void addUser(User user) throws SQLException, InterruptedException;

  void changeUsersGold(long userId, int gold) throws InterruptedException, SQLException;
}
