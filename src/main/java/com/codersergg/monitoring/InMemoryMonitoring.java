package com.codersergg.monitoring;

import com.codersergg.monitoring.model.Metric;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryMonitoring implements Monitoring {

  private static final AtomicLong count  = new AtomicLong(0);

  Map <Long, Metric> metricMap = new ConcurrentHashMap<>(10_000);

  @Override
  public void send(Metric metric) {
    metricMap.put(count.incrementAndGet(), metric);
  }

  @Override
  public Map<Long, Metric> getMetricMap() {
    return metricMap;
  }
}
