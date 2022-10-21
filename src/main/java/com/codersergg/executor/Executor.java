package com.codersergg.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Executor {

  public ExecutorService getExecutorService() {
    final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(10_000_000);
    return new ThreadPoolExecutor(16, 32, 5L, TimeUnit.MILLISECONDS, queue);
  }

}
