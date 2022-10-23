package com.codersergg.monitoring;

import com.codersergg.monitoring.model.Metric;
import com.codersergg.monitoring.producer.MonitoringKafkaProducer;

public class KafkaMonitoring implements Monitoring {

  private final MonitoringKafkaProducer producer;

  public KafkaMonitoring(MonitoringKafkaProducer producer) {
    this.producer = producer;
  }

  @Override
  public void send(Metric metric) {
    producer.send(metric);
  }
}
