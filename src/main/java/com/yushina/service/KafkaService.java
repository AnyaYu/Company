package com.yushina.service;

import com.yushina.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
    @Autowired
    private KafkaTemplate<String, Employee> kafkaTemplate;

    @Async
    public void sendKafkaMessage(String message, Employee employee) {
        kafkaTemplate.send("employee-events", message, employee);
    }
}
