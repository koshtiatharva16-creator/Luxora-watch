package com.luxora.config;

import com.luxora.model.Watch;
import com.luxora.repository.WatchRepository;
import com.luxora.repository.UserRepository;
import com.luxora.model.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;

@Configuration
public class SeedData {
        @Bean
        CommandLineRunner seedWatches(WatchRepository repo, UserRepository users,
                        @Value("${luxora.admin.email:admin@luxora.com}") String adminEmail,
                        @Value("${luxora.admin.password:LuxoraAdmin@2026}") String adminPassword) {
                return args -> {
                        // Create the default administrator once. Never overwrite an existing
                        // account/password.
                        if (!users.existsByEmailIgnoreCase(adminEmail)) {
                                AppUser admin = new AppUser();
                                admin.setFullName("Luxora Administrator");
                                admin.setEmail(adminEmail.trim().toLowerCase());
                                admin.setPasswordHash(new BCryptPasswordEncoder().encode(adminPassword));
                                admin.setRole("ADMIN");
                                users.save(admin);
                        }
                        if (repo.count() > 0)
                                return;
                        String[] images = {
                                        "https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=1000&q=85",
                                        "https://images.unsplash.com/photo-1524805444758-089113d48a6d?auto=format&fit=crop&w=1000&q=85",
                                        "https://images.unsplash.com/photo-1524592094714-0f0654e20314?auto=format&fit=crop&w=1000&q=85",
                                        "https://images.unsplash.com/photo-1523170335258-f5ed11844a49?auto=format&fit=crop&w=1000&q=85",
                                        "https://images.unsplash.com/photo-1508685096489-7aacd43bd3b1?auto=format&fit=crop&w=1000&q=85",
                                        "https://images.unsplash.com/photo-1547996160-81dfa63595aa?auto=format&fit=crop&w=1000&q=85",
                                        "https://images.unsplash.com/photo-1434056886845-dac89ffe9b56?auto=format&fit=crop&w=1000&q=85",
                                        "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?auto=format&fit=crop&w=1000&q=85",
                                        "https://images.unsplash.com/photo-1614164185128-e4ec99c436d7?auto=format&fit=crop&w=1000&q=85",
                                        "https://images.unsplash.com/photo-1522312346375-d1a52e2b99b3?auto=format&fit=crop&w=1000&q=85",
                                        "https://images.unsplash.com/photo-1612817288484-6f916006741a?auto=format&fit=crop&w=1000&q=85",
                                        "https://images.unsplash.com/photo-1619134778706-7015533a6150?auto=format&fit=crop&w=1000&q=85",
                                        "https://images.unsplash.com/photo-1508057198894-247b23fe5ade?auto=format&fit=crop&w=1000&q=85",
                                        "https://images.unsplash.com/photo-1526045431048-f857369baa09?auto=format&fit=crop&w=1000&q=85",
                                        "https://images.unsplash.com/photo-1506629905607-d9a4c3b0b6b8?auto=format&fit=crop&w=1000&q=85"
                        };
                        String[][] data = {
                                        { "LUXORA Heritage Chronograph", "Luxora", "Heritage",
                                                        "A bold chronograph with a deep black dial, polished gold accents and a refined leather strap.",
                                                        "1299", "1599", "Automatic", "42 mm", "Leather", "5 ATM",
                                                        "true" },
                                        { "Midnight Classic", "Luxora", "Classic",
                                                        "A timeless everyday watch with a midnight dial and a clean stainless-steel bracelet.",
                                                        "899", "1099", "Quartz", "40 mm", "Steel", "5 ATM", "true" },
                                        { "Aurelius Gold Automatic", "Aurelius", "Heritage",
                                                        "An elegant automatic timepiece with warm gold detailing and an exhibition-inspired aesthetic.",
                                                        "1499", "1799", "Automatic", "43 mm", "Leather", "10 ATM",
                                                        "true" },
                                        { "Ocean Blue Diver", "Mariner", "Sport",
                                                        "A confident blue-dial sport watch designed for weekend adventures and everyday wear.",
                                                        "749", "899", "Automatic", "44 mm", "Steel", "20 ATM",
                                                        "false" },
                                        { "Onyx Minimal", "Luxora", "Classic",
                                                        "Minimalist markers, a slim case and a versatile black strap for understated style.",
                                                        "499",
                                                        "599", "Quartz", "38 mm", "Leather", "3 ATM", "false" },
                                        { "Royal Skeleton", "Aurelius", "Heritage",
                                                        "An intricate skeleton-inspired face framed by a brushed steel case and premium strap.",
                                                        "1899", "2199", "Automatic", "42 mm", "Leather", "5 ATM",
                                                        "true" },
                                        { "Racing Steel", "Velocity", "Sport",
                                                        "Sporty chronograph styling with a tachymeter-inspired bezel and precision timing layout.",
                                                        "999", "1199", "Quartz", "44 mm", "Steel", "10 ATM", "false" },
                                        { "Rose Elegance", "LUXORA Atelier", "Ladies",
                                                        "A delicate rose-gold finish and ivory dial bring graceful sophistication to special occasions.",
                                                        "679", "799", "Quartz", "34 mm", "Steel", "3 ATM", "false" },
                                        { "Carbon Pro", "Velocity", "Sport",
                                                        "A modern blacked-out design with a bold profile for performance-minded watch lovers.",
                                                        "1099", "1299", "Automatic", "45 mm", "Silicone", "20 ATM",
                                                        "false" },
                                        { "Champagne Dress Watch", "Aurelius", "Classic",
                                                        "A champagne dial and polished bracelet make this a refined choice for formal looks.",
                                                        "829", "999", "Quartz", "39 mm", "Steel", "5 ATM", "false" },
                                        { "Pearl Moonlight", "LUXORA Atelier", "Ladies",
                                                        "Soft metallic tones and a luminous dial create a sophisticated, jewellery-inspired silhouette.",
                                                        "729", "849", "Quartz", "32 mm", "Steel", "3 ATM", "false" },
                                        { "Heritage Brown Leather", "Luxora", "Heritage",
                                                        "Warm brown leather, a brushed case and vintage-inspired detailing with modern reliability.",
                                                        "1199", "1399", "Automatic", "41 mm", "Leather", "5 ATM",
                                                        "false" },
                                        { "Expedition Field Watch", "Mariner", "Sport",
                                                        "Highly legible numerals and a durable strap for outdoor days and practical daily wear.",
                                                        "589", "699", "Quartz", "42 mm", "Nylon", "10 ATM", "false" },
                                        { "Lunar Silver", "Luxora", "Classic",
                                                        "A silver-tone case with understated dial detailing and a comfortable everyday profile.",
                                                        "949", "1099", "Automatic", "40 mm", "Steel", "5 ATM",
                                                        "false" },
                                        { "Celestial Limited Edition", "Aurelius", "Limited Edition",
                                                        "A statement collector piece with rich gold accents and a distinctive, dramatic dial.",
                                                        "2499", "2899", "Automatic", "43 mm", "Leather", "5 ATM",
                                                        "true" }
                        };
                        for (int i = 0; i < data.length; i++) {
                                String[] d = data[i];
                                repo.save(new Watch(d[0], d[1], d[2], d[3], new BigDecimal(d[4]), new BigDecimal(d[5]),
                                                images[i], d[6],
                                                d[7], d[8], d[9], Boolean.parseBoolean(d[10]), 20));
                        }
                };
        }
}
