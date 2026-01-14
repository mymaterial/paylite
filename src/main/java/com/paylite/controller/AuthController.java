package com.paylite.controller;

import com.paylite.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;

@Controller
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            RedirectAttributes redirectAttributes) {

        if (userRepository.userExists(username)) {
            redirectAttributes.addFlashAttribute(
                "error",
                "Username already exists"
            );
            return "redirect:/register";
        }

        userRepository.createUser(username, password);

        redirectAttributes.addFlashAttribute(
            "message",
            "Registration successful. Please login."
        );

        return "redirect:/login";
    }
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        Long userId = userRepository.validateUser(username, password);

        if (userId == null) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }

        session.setAttribute("userId", userId);
        session.setAttribute("username", username);

        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

}
