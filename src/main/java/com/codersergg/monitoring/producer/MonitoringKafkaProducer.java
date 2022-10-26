package com.codersergg.monitoring.producer;

import com.codersergg.executor.AppExecutor;
import com.codersergg.monitoring.InMemoryMonitoring;
import com.codersergg.monitoring.model.Event;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import lombok.extern.java.Log;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;

@Log
public class MonitoringKafkaProducer {

  private static final ExecutorService executorService = AppExecutor.getKafkaExecutorService();
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
    executorService.submit(() -> {
      try {
        loadingFromMemoryIntoKafka();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });
  }

  public void send(Event event) {
    ProducerRecord<String, String> record = new ProducerRecord<>("monitoring",
        String.valueOf(event.getClanId()), event.toString());
    if (isNoAvailable()) {
      memoryMonitoring.send(event);
      log.info("the event: " + event
          + " was not sent on the first attempt and saved in the backup service");
      log.info("backup service size: " + memoryMonitoring.getEventQueue().size());
    } else {
      try {
        producer.send(record);
        log.info("event: " + event + " sent on first try");
      } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
        memoryMonitoring.send(event);
        log.info("backup service size: " + memoryMonitoring.getEventQueue().size());
      } catch (KafkaException e) {
        secondTrySend(record, event);
      } catch (Exception e) {
        secondTrySend(record, event);
        log.info("the event: " + event
            + " was not sent on the first attempt and saved in the backup service");
      }
    }
  }

  private void secondTrySend(ProducerRecord<String, String> record, Event event) {
    if (isNoAvailable()) {
      memoryMonitoring.send(event);
      log.info("the event: " + event
          + " was not sent on the second attempt and saved in the backup service");
      log.info("backup service size: " + memoryMonitoring.getEventQueue().size());
    } else {
      try {
        producer.send(record);
        log.info("event: " + event + " sent on second try");
      } catch (ProducerFencedException | OutOfOrderSequenceException e) {
        memoryMonitoring.send(event);
        log.info("backup service size: " + memoryMonitoring.getEventQueue().size());
      } catch (KafkaException e) {
        memoryMonitoring.send(event);
        log.info("the event: " + event
            + " was not sent on the second attempt and saved in the backup service");
        log.info("backup service size: " + memoryMonitoring.getEventQueue().size());
      } catch (Exception e) {
        memoryMonitoring.send(event);
        log.info("backup service size: " + memoryMonitoring.getEventQueue().size());
      }
    }
  }

  public boolean isNoAvailable() {
    try (AdminClient client = KafkaAdminClient.create(properties)) {
      return client.listTopics(new ListTopicsOptions().timeoutMs(300)).listings().get().isEmpty();
    } catch (InterruptedException | ExecutionException e) {
      return true;
    }
  }

  private void loadingFromMemoryIntoKafka() throws InterruptedException {
    Queue<Event> eventQueue = memoryMonitoring.getEventQueue();
    int size = eventQueue.size();
    if (!isNoAvailable() && size != 0) {
      eventQueue.forEach(this::send);
      log.info("loading from memory into Kafka: " + size);
    } else {
      Thread.sleep(10_000);
      loadingFromMemoryIntoKafka();
    }
  }
}
