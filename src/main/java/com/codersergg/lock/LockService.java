package com.codersergg.lock;

import java.util.concurrent.locks.Lock;

public interface LockService {

  Lock getLock(Lockable objToLock);

}
