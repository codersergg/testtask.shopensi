package com.codersergg.lock;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.java.Log;

@Log
public class LockServiceImpl implements LockService {

  private final Map<Object, Object> lockMap = new HashMap<>();

  @Override
  public Object getLock(Object objToLock) {
    Object lock;
    synchronized (lockMap) {
      lock = lockMap.computeIfAbsent(objToLock, k -> new Object());
    }
    log.info("lockMap.size: "+ lockMap.size());
    log.info("lock: "+ lock);
    return lock;
  }
}
