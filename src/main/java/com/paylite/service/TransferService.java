package com.paylite.service;

import com.paylite.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    private final UserRepository userRepository;

    public TransferService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void transferMoney(
            Long fromUserId,
            String toUsername,
            BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        Long toUserId = userRepository.getUserIdByUsername(toUsername);
        if (toUserId == null) {
            throw new RuntimeException("Unable to process transfer. Please verify the details and try again.");
        }

        // Lock sender row
        BigDecimal senderBalance =
                userRepository.getBalanceForUpdate(fromUserId);

        if (senderBalance.compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Deduct sender
        userRepository.updateBalance(fromUserId, amount.negate());

        // Credit receiver
        userRepository.updateBalance(toUserId, amount);
    }
}
