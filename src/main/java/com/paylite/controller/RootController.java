package com.paylite.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/")
    public String root(HttpSession session) {

        if (session.getAttribute("userId") != null) {
            return "redirect:/home";
        }

        return "redirect:/login";
    }
}
