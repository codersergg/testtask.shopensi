package com.codersergg.lock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.java.Log;

@Log
public class LockServiceImpl implements LockService {

  private final Map<Lockable, Lock> lockMap = new ConcurrentHashMap<>(1000);

  @Override
  public Lock getLock(Lockable objToLock) {
    Lock lock = lockMap.computeIfAbsent(objToLock, k -> new ReentrantLock());
    log.info("lockMap.size: " + lockMap.size());
    log.info("lock: " + lock);
    return lock;
  }
}
