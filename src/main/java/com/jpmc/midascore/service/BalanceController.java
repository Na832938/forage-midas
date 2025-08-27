package com.jpmc.midascore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;

@RestController
public class BalanceController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/balance")
    public Balance getBalance(@RequestParam String userId) {
        
        UserRecord matchedUser = userRepository.findById(Long.parseLong(userId));
        if (matchedUser == null) {
            return new Balance(0);
        } else {
            Balance newBalance = new Balance(matchedUser.getBalance());
            return newBalance;
        }
        
    }
    
}
