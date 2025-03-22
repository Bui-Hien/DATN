package com.buihien.datn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/auth")
public class ForgetPasswordController {
//    "http://localhost:8080/auth/reset-password?resetToken=abc123";
    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam("resetToken") String resetToken, Model model) {
        model.addAttribute("resetToken", resetToken); // Thêm resetToken vào model
        return "reset-password"; // Trả về template Thymeleaf
    }

}