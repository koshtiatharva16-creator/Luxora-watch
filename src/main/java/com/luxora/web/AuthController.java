package com.luxora.web;

import com.luxora.model.AppUser;
import com.luxora.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final UserService users;

    public AuthController(UserService users) {
        this.users = users;
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String next, Model model) {
        model.addAttribute("next", next == null ? "" : next);
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String email, @RequestParam String password,
            @RequestParam(required = false) String next, HttpSession session, Model model) {
        var found = users.authenticate(email, password);
        if (found.isEmpty()) {
            model.addAttribute("error", "Email or password is incorrect.");
            model.addAttribute("next", next == null ? "" : next);
            return "login";
        }
        AppUser user = found.get();
        session.setAttribute("currentUser", user);
        if ("ADMIN".equals(user.getRole()))
            return "redirect:/admin";
        if ("checkout".equals(next))
            return "redirect:/checkout";
        return "redirect:/";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@RequestParam String fullName, @RequestParam String email,
            @RequestParam String password, @RequestParam String confirmPassword, Model model, HttpSession session) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "register";
        }
        try {
            AppUser user = users.register(fullName, email, password);
            session.setAttribute("currentUser", user);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
