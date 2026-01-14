package com.paylite.controller;

import com.paylite.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;

@Controller
public class HomeController {

    private final UserRepository userRepository;

    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {

        Long userId = (Long) session.getAttribute("userId");
        String username = (String) session.getAttribute("username");

        if (userId == null) {
            return "redirect:/login";
        }

        BigDecimal balance = userRepository.getBalance(userId);

        model.addAttribute("username", username);
        model.addAttribute("balance", balance);

        return "home";
    }
}
