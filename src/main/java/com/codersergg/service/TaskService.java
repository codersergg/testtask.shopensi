package com.codersergg.service;

import com.codersergg.lock.LockService;
import com.codersergg.model.Task;
import com.codersergg.monitoring.Monitoring;
import com.codersergg.monitoring.model.GoldValues;
import com.codersergg.monitoring.model.Metric;
import com.codersergg.monitoring.model.Operation;
import com.codersergg.repository.TaskRepository;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import lombok.extern.java.Log;

// Еще один такой сервис
@Log
public class TaskService { // какой-то сервис с заданиями

  private final LockService lockService;
  private final ClanService clans;
  private final TaskRepository taskRepository;
  private final Monitoring monitoring;
  private final ExecutorService executor;

  public TaskService(LockService lockService, ClanService clans,
      TaskRepository taskRepository, Monitoring monitoring, ExecutorService executor) {
    this.lockService = lockService;
    this.clans = clans;
    this.taskRepository = taskRepository;
    this.monitoring = monitoring;
    this.executor = executor;
  }

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
        GoldValues goldValues = clans.changeClansGold(clanId, task.getGold());

        executor.submit(() -> monitoring.send(Metric.builder()
            .dateTime(goldValues.getDateTime())
            .clanId(clanId)
            .amountGoldBeforeRaise(goldValues.getAmountGoldBeforeRaise())
            .amountGoldToRaise(goldValues.getAmountGoldToRaise())
            .amountGoldAfterRaise(goldValues.getAmountGoldAfterRaise())
            .operation(Operation.TASK)
            .message("taskId: " + taskId)
            .build()));

      }
    }
  }

  private int changeTaskProgress(long taskId) throws SQLException, InterruptedException {
    taskRepository.deductTaskProgress(taskId);
    return getTask(taskId).orElseThrow().getProgress();
  }

  private void checkProgress(long taskId) throws SQLException, InterruptedException {
    if (taskRepository.getTask(taskId).orElseThrow().getProgress() <= 0) {
      throw new IllegalStateException("Clan quest completed");
    }
  }

  public void addTask(Task task) throws SQLException, InterruptedException {
    taskRepository.addTask(task);
  }

  public Optional<Task> getTask(long taskId) throws SQLException, InterruptedException {
    return taskRepository.getTask(taskId);
  }
}
