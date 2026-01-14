package com.paylite.controller;

import com.paylite.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController {

    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/update-phone")
    public String updatePhone(
            @RequestParam String phoneNumber,
            HttpSession session,
            Model model) {

        Long userId = (Long) session.getAttribute("userId");
        String username = (String) session.getAttribute("username");

        if (userId == null) {
            return "redirect:/login";
        }

        try {
            userRepository.updatePhoneNumber(userId, phoneNumber);
            model.addAttribute("message", "Phone number updated successfully");
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update phone number");
        }

        model.addAttribute("username", username);
        model.addAttribute("balance", userRepository.getBalance(userId));

        return "home";
    }
}
