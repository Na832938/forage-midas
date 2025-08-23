package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRecordRepository;

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

        updateBalances(sender, recipient, transaction.getAmount());

        return true;

    }

    private void updateBalances(UserRecord sender, UserRecord recipient, float amount) {
        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount);
        userRepository.save(sender);
        userRepository.save(recipient);
    }
    public void recordTransaction(UserRecord sender, UserRecord recipient, float amount) {
        TransactionRecord record = new TransactionRecord(sender, recipient, amount);
        transactionRecordRepository.save(record);
    }
}

