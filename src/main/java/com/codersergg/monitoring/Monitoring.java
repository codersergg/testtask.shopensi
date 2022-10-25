package com.codersergg.monitoring;

import com.codersergg.monitoring.model.Event;

public interface Monitoring {

  void send(Event event);

}
