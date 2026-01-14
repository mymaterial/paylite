package com.paylite.controller;

import com.paylite.service.TransferService;
import com.paylite.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class TransferController {

    private final TransferService transferService;
    private final UserRepository userRepository;

    public TransferController(
            TransferService transferService,
            UserRepository userRepository) {
        this.transferService = transferService;
        this.userRepository = userRepository;
    }

    @PostMapping("/transfer")
    public String transfer(
            @RequestParam String toUsername,
            @RequestParam BigDecimal amount,
            HttpSession session,
            Model model) {

        Long fromUserId = (Long) session.getAttribute("userId");
        String username = (String) session.getAttribute("username");

        if (fromUserId == null) {
            return "redirect:/login";
        }

        try {
            transferService.transferMoney(fromUserId, toUsername, amount);
            model.addAttribute("message", "Transfer successful");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("username", username);
        model.addAttribute("balance",
                userRepository.getBalance(fromUserId));

        return "home";
    }
}
