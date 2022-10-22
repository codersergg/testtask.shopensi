package com.codersergg.service;

import com.codersergg.lock.LockService;
import com.codersergg.model.Task;
import com.codersergg.repository.TaskRepository;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.extern.java.Log;

// Еще один такой сервис
@Log
public class TaskServiceImpl implements TaskService { // какой-то сервис с заданиями

  private final LockService lockService;
  private final ClanService clans;
  private final TaskRepository taskRepository;

  public TaskServiceImpl(LockService lockService, ClanService clans,
      TaskRepository taskRepository) {
    this.lockService = lockService;
    this.clans = clans;
    this.taskRepository = taskRepository;
  }

  @Override
  public void completeTask(long clanId, long taskId) throws SQLException, InterruptedException {

    Task task;
    synchronized (lockService.getLock(getTask(taskId).orElseThrow())) {
      task = taskRepository.getTask(taskId).orElseThrow();
      checkProgress(task.getId());
    }

    // Эмитация выполнения задания таски пользователем
    LocalDateTime startTime = LocalDateTime.now();
    while (true) {
      if (startTime.plusSeconds(task.getGold() / 10 + 1).isAfter(LocalDateTime.now())) {
        break;
      }
    }

    synchronized (lockService.getLock(getTask(taskId).orElseThrow())) {
      checkProgress(task.getId());
      int reducedProgress = changeTaskProgress(taskId);
      if (reducedProgress == 0) {
        clans.changeClansGold(clanId, task.getGold());
      }
    }
  }

  public int changeTaskProgress(long taskId) throws SQLException, InterruptedException {
    taskRepository.deductTaskProgress(taskId);
    return getTask(taskId).orElseThrow().getProgress();
  }

  private void checkProgress(long taskId) throws SQLException, InterruptedException {
    if (taskRepository.getTask(taskId).orElseThrow().getProgress() <= 0) {
      throw new IllegalStateException("Clan quest completed");
    }
  }

  @Override
  public void addTask(Task task) throws SQLException, InterruptedException {
    taskRepository.addTask(task);
  }

  @Override
  public Optional<Task> getTask(long taskId) throws SQLException, InterruptedException {
    return taskRepository.getTask(taskId);
  }
}
