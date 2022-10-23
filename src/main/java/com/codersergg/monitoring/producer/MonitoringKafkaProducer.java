package com.codersergg.monitoring.producer;

import com.codersergg.monitoring.InMemoryMonitoring;
import com.codersergg.monitoring.model.Metric;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import lombok.extern.java.Log;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;

@Log
public class MonitoringKafkaProducer {

  private final Producer<String, String> producer;

  private final InMemoryMonitoring memoryMonitoring;

  private final Properties properties = new Properties();

  public MonitoringKafkaProducer(InMemoryMonitoring memoryMonitoring) {
    properties.put("bootstrap.servers", "localhost:29092");
    properties.put("retries", 0);
    properties.put("batch.size", 16384);
    properties.put("buffer.memory", 33554432);
    properties.put("linger.ms", 1);
    properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    this.producer = new KafkaProducer<>(properties);
    this.memoryMonitoring = memoryMonitoring;
  }

  public void send(Metric metric) {
    ProducerRecord<String, String> record = new ProducerRecord<>("monitoring",
        String.valueOf(metric.getClanId()), metric.toString());
    if (isNoAvailable()) {
      memoryMonitoring.send(metric);
      log.info("the metric: " + metric
          + " was not sent on the first attempt and saved in the backup service");
      log.info("backup service size: " + memoryMonitoring.getMetricMap().size());
    } else {
      try {
        producer.send(record);
        log.info("metric: " + metric + " sent on first try");
      } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
        memoryMonitoring.send(metric);
        log.info("backup service size: " + memoryMonitoring.getMetricMap().size());
      } catch (KafkaException e) {
        secondTrySend(record, metric);
      } catch (Exception e) {
        secondTrySend(record, metric);
        log.info("the metric: " + metric
            + " was not sent on the first attempt and saved in the backup service");
      }
    }
  }


  private void secondTrySend(ProducerRecord<String, String> record, Metric metric) {
    if (isNoAvailable()) {
      memoryMonitoring.send(metric);
      log.info("the metric: " + metric
          + " was not sent on the second attempt and saved in the backup service");
      log.info("backup service size: " + memoryMonitoring.getMetricMap().size());
    } else {
      try {
        producer.send(record);
        log.info("metric: " + metric + " sent on second try");
      } catch (ProducerFencedException | OutOfOrderSequenceException e) {
        memoryMonitoring.send(metric);
        log.info("backup service size: " + memoryMonitoring.getMetricMap().size());
      } catch (KafkaException e) {
        memoryMonitoring.send(metric);
        log.info("the metric: " + metric
            + " was not sent on the second attempt and saved in the backup service");
        log.info("backup service size: " + memoryMonitoring.getMetricMap().size());
      }
    }
  }

  private boolean isNoAvailable() {
    try (AdminClient client = KafkaAdminClient.create(properties)) {
      ListTopicsResult topics = client.listTopics();
      Set<String> names = topics.names().get();
      // is topic found?
      return names.isEmpty();
    } catch (InterruptedException | ExecutionException e) {
      // Kafka is not available
      return true;
    }
  }

}