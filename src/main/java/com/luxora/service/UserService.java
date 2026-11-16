package com.luxora.service;

import com.luxora.model.AppUser;
import com.luxora.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository users;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository users) {
        this.users = users;
    }

    public AppUser register(String name, String email, String password) {
        String cleanEmail = email.trim().toLowerCase();
        if (users.existsByEmailIgnoreCase(cleanEmail))
            throw new IllegalArgumentException("An account with this email already exists.");
        if (password.length() < 8)
            throw new IllegalArgumentException("Password must contain at least 8 characters.");
        AppUser u = new AppUser();
        u.setFullName(name.trim());
        u.setEmail(cleanEmail);
        u.setPasswordHash(encoder.encode(password));
        u.setRole("USER");
        return users.save(u);
    }

    public Optional<AppUser> authenticate(String email, String password) {
        return users.findByEmailIgnoreCase(email.trim()).filter(u -> encoder.matches(password, u.getPasswordHash()));
    }

    public Optional<AppUser> find(Long id) {
        return users.findById(id);
    }

    public AppUser update(AppUser user) {
        return users.save(user);
    }

    public long count() {
        return users.count();
    }
}
