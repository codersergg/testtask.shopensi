package com.codersergg.lock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.java.Log;

@Log
public class LockServiceImpl implements LockService {

  private final Map<Object, Object> lockMap = new ConcurrentHashMap<>(1000);

  @Override
  public Object getLock(Object objToLock) {
    Object lock = lockMap.computeIfAbsent(objToLock, k -> new Object());
    log.info("lockMap.size: " + lockMap.size());
    log.info("lock: " + lock);
    return lock;
  }
}
