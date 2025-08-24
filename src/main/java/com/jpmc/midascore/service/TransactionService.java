package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRecordRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    public boolean processTransaction(Transaction transaction) {

        UserRecord sender = userRepository.findById(transaction.getSenderId());
        if (sender == null) {
            return false;
        }

        UserRecord recipient = userRepository.findById(transaction.getRecipientId());
        if (recipient == null) {
            return false;
        }

        if (sender.getBalance() < transaction.getAmount()) {
            return false;
        }

        Incentive incentive = restTemplate.postForObject(
                "http://localhost:8080/incentive",
                transaction,
                Incentive.class
        );

        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentive.getAmount());
        userRepository.save(sender);
        userRepository.save(recipient);
        recordTransaction(sender, recipient, transaction.getAmount(), incentive.getAmount());

        System.out.println(incentive.getAmount());
        UserRecord waldorf = userRepository.findByName("waldorf");
        UserRecord wilbur = userRepository.findByName("wilbur");
        System.out.println("waldorf: " + (int) Math.floor(waldorf.getBalance()));
        System.out.println("wilbur: " + (int) Math.floor(wilbur.getBalance()));

        return true;

    }

    public void recordTransaction(UserRecord sender, UserRecord recipient, float amount, float incentive) {
        TransactionRecord record = new TransactionRecord(sender, recipient, amount, incentive);
        transactionRecordRepository.save(record);
    }
}

