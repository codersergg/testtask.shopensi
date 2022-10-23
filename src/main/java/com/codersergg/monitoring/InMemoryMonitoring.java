package com.codersergg.monitoring;

import com.codersergg.monitoring.model.Metric;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.java.Log;

@Log
public class InMemoryMonitoring {

  private static final AtomicLong count  = new AtomicLong(0);

  Map <Long, Metric> metricMap = new ConcurrentHashMap<>(10_000);

  public void send(Metric metric) {
    metricMap.put(count.incrementAndGet(), metric);
    log.info(metric.toString());
  }

  public Map<Long, Metric> getMetricMap() {
    return metricMap;
  }
}
