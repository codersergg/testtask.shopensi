package com.codersergg.monitoring;

import com.codersergg.monitoring.model.Metric;

public interface Monitoring {

  void send(Metric metric);

}
