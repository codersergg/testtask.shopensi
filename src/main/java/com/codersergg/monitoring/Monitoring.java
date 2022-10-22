package com.codersergg.monitoring;

import com.codersergg.monitoring.model.Metric;
import java.util.Map;

public interface Monitoring {

  void send(Metric metric);

  Map<Long, Metric> getMetricMap();
}
