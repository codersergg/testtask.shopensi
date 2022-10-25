package com.codersergg.monitoring;

import com.codersergg.monitoring.model.Event;
import com.codersergg.monitoring.producer.MonitoringKafkaProducer;

public class KafkaMonitoring implements Monitoring {

  private final MonitoringKafkaProducer producer;

  public KafkaMonitoring(MonitoringKafkaProducer producer) {
    this.producer = producer;
  }

  @Override
  public void send(Event event) {
    producer.send(event);
  }
}
