package com.jpmc.midascore;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.jpmc.midascore.foundation.Transaction;

@Component
public class KafkaConsumer {
    
    @KafkaListener(topics = "${midascore.topic}")
    public void processMessage(Transaction content) {
        System.out.println("test");
    }
}
