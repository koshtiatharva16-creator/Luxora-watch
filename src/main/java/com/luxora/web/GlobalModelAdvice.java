package com.luxora.web;

import com.luxora.model.AppUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {
    @ModelAttribute("currentUser")
    public AppUser currentUser(HttpSession session) {
        Object user = session.getAttribute("currentUser");
        return user instanceof AppUser ? (AppUser) user : null;
    }
}
