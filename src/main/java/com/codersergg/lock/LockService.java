package com.codersergg.lock;

public interface LockService {

  Object getLock(Lockable objToLock);

}
