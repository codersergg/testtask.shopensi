package com.codersergg.service;

import com.codersergg.model.Task;
import java.sql.SQLException;
import java.util.Optional;

public interface TaskService {

  void completeTask(long clanId, long taskId) throws SQLException, InterruptedException;

  void addTask(Task task) throws SQLException, InterruptedException;

  Optional<Task> getTask(long taskId) throws SQLException, InterruptedException;
}
