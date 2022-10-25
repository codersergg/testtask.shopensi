package com.codersergg.monitoring;

import com.codersergg.monitoring.model.Event;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.extern.java.Log;

@Log
public class InMemoryMonitoring implements Monitoring {

  private final Queue<Event> eventQueue = new ConcurrentLinkedQueue<>();

  public void send(Event event) {
    eventQueue.add(event);
    log.info(event.toString());
  }

  public Queue<Event> getEventQueue() {
    return eventQueue;
  }
}
