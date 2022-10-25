package com.codersergg.monitoring;

import com.codersergg.monitoring.model.Metric;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.extern.java.Log;

@Log
public class InMemoryMonitoring implements Monitoring {

  private final Queue<Metric> metricQueue = new ConcurrentLinkedQueue<>();

  public void send(Metric metric) {
    metricQueue.add(metric);
    log.info(metric.toString());
  }

  public Queue<Metric> getMetricQueue() {
    return metricQueue;
  }
}
