package com.jpmc.midascore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.jpmc.midascore.foundation.Transaction;

@Component
public class KafkaConsumer {

    @Autowired
    private TransactionService transactionService;

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "my-group")
    public void listen(Transaction message) {

        boolean processed = transactionService.processTransaction(message);

        if (processed == true) {
            System.out.println("Completed transaction: " + message);
        } else {
            System.out.println("Removed invalid transaction: " + message);
        }

        System.out.println("Received: " + message);
    }

}
