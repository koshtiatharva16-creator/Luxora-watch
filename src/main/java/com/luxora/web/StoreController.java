package com.luxora.web;

import com.luxora.model.*;
import com.luxora.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.*;

@Controller
public class StoreController {
    private final WatchService watches;
    private final OrderService orders;
    private final UserService users;

    public StoreController(WatchService watches, OrderService orders, UserService users) {
        this.watches = watches;
        this.orders = orders;
        this.users = users;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("featured", watches.featured());
        model.addAttribute("watches", watches.all().stream().limit(8).toList());
        return "index";
    }

    @GetMapping("/collections")
    public String collections(@RequestParam(required = false) String category,
            @RequestParam(required = false) String q, @RequestParam(required = false) String sort, Model model) {
        List<Watch> list = (q != null && !q.isBlank()) ? watches.search(q) : watches.category(category);
        if ("price-asc".equals(sort))
            list.sort(Comparator.comparing(Watch::getPrice));
        if ("price-desc".equals(sort))
            list.sort(Comparator.comparing(Watch::getPrice).reversed());
        if ("name".equals(sort))
            list.sort(Comparator.comparing(Watch::getName));
        model.addAttribute("watches", list);
        model.addAttribute("category", category);
        model.addAttribute("q", q);
        model.addAttribute("sort", sort);
        return "collections";
    }

    @GetMapping("/watch/{id}")
    public String details(@PathVariable Long id, Model model) {
        model.addAttribute("watch", watches.get(id));
        return "watch-details";
    }

    @GetMapping("/brands")
    public String brands(Model model) {
        model.addAttribute("watches", watches.all());
        return "brands";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @PostMapping("/contact")
    public String contactSubmit(@RequestParam String name, @RequestParam String email,
            @RequestParam String subject, @RequestParam String message, Model model) {
        model.addAttribute("sent", true);
        return "contact";
    }

    @SuppressWarnings("unchecked")
    private Map<Long, Integer> cart(HttpSession session) {
        Object obj = session.getAttribute("cart");
        if (obj instanceof Map<?, ?>)
            return (Map<Long, Integer>) obj;
        Map<Long, Integer> map = new LinkedHashMap<>();
        session.setAttribute("cart", map);
        return map;
    }

    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable Long id, HttpSession session) {
        Watch w = watches.get(id);
        if (w.getStock() > 0)
            cart(session).merge(id, 1, Integer::sum);
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String cartPage(HttpSession session, Model model) {
        Map<Long, Integer> cart = cart(session);
        List<Map<String, Object>> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (var e : cart.entrySet()) {
            try {
                Watch w = watches.get(e.getKey());
                int qty = e.getValue();
                BigDecimal line = w.getPrice().multiply(BigDecimal.valueOf(qty));
                total = total.add(line);
                items.add(Map.of("watch", w, "quantity", qty, "lineTotal", line));
            } catch (Exception ignored) {
            }
        }
        model.addAttribute("items", items);
        model.addAttribute("total", total);
        return "cart";
    }

    @PostMapping("/cart/update")
    public String updateCart(@RequestParam Long watchId, @RequestParam int quantity, HttpSession session) {
        Map<Long, Integer> c = cart(session);
        if (quantity <= 0)
            c.remove(watchId);
        else
            c.put(watchId, Math.min(quantity, 20));
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove/{id}")
    public String removeCart(@PathVariable Long id, HttpSession session) {
        cart(session).remove(id);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        AppUser u = (AppUser) session.getAttribute("currentUser");
        if (u == null)
            return "redirect:/login?next=checkout";
        if (cart(session).isEmpty())
            return "redirect:/collections";
        model.addAttribute("user", u);
        model.addAttribute("cartCount", cart(session).values().stream().mapToInt(Integer::intValue).sum());
        model.addAttribute("total", cartTotal(cart(session)));
        return "checkout";
    }

    private BigDecimal cartTotal(Map<Long, Integer> cart) {
        BigDecimal total = BigDecimal.ZERO;
        for (var e : cart.entrySet())
            total = total.add(watches.get(e.getKey()).getPrice().multiply(BigDecimal.valueOf(e.getValue())));
        return total;
    }

    @PostMapping("/checkout")
    public String placeOrder(@RequestParam String customerName, @RequestParam String email,
            @RequestParam String phone, @RequestParam String address, @RequestParam String city,
            @RequestParam String postalCode,
            HttpSession session, Model model) {
        AppUser u = (AppUser) session.getAttribute("currentUser");
        if (u == null)
            return "redirect:/login";
        Map<Long, Integer> cart = cart(session);
        if (cart.isEmpty())
            return "redirect:/collections";
        StoreOrder order = new StoreOrder();
        order.setUserId(u.getId());
        order.setCustomerName(customerName);
        order.setEmail(email);
        order.setShippingAddress(address + ", " + city + " " + postalCode + " | Phone: " + phone);
        order.setTotal(cartTotal(cart));
        StringJoiner summary = new StringJoiner(", ");
        for (var e : cart.entrySet())
            summary.add(watches.get(e.getKey()).getName() + " x " + e.getValue());
        order.setItemsSummary(summary.toString());
        orders.save(order);
        session.setAttribute("lastOrderId", order.getId());
        cart.clear();
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orders(HttpSession session, Model model) {
        AppUser u = (AppUser) session.getAttribute("currentUser");
        if (u == null)
            return "redirect:/login";
        model.addAttribute("orders", orders.forUser(u.getId()));
        return "orders";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        AppUser u = (AppUser) session.getAttribute("currentUser");
        if (u == null)
            return "redirect:/login";
        model.addAttribute("user", u);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam String fullName, @RequestParam(defaultValue = "") String phone,
            @RequestParam(defaultValue = "") String address, HttpSession session) {
        AppUser u = (AppUser) session.getAttribute("currentUser");
        if (u == null)
            return "redirect:/login";
        u.setFullName(fullName.trim());
        u.setPhone(phone);
        u.setAddress(address);
        users.update(u);
        session.setAttribute("currentUser", u);
        return "redirect:/profile?updated";
    }

    @SuppressWarnings("unchecked")
    private Set<Long> wishlist(HttpSession s) {
        Object o = s.getAttribute("wishlist");
        if (o instanceof Set<?>)
            return (Set<Long>) o;
        Set<Long> set = new LinkedHashSet<>();
        s.setAttribute("wishlist", set);
        return set;
    }

    @PostMapping("/wishlist/toggle/{id}")
    public String toggleWishlist(@PathVariable Long id, HttpSession s,
            @RequestHeader(value = "Referer", required = false) String referer) {
        Set<Long> set = wishlist(s);
        if (!set.add(id))
            set.remove(id);
        return "redirect:" + (referer != null && referer.contains("/watch/") ? referer : "/wishlist");
    }

    @GetMapping("/wishlist")
    public String wishlistPage(HttpSession s, Model model) {
        List<Watch> list = new ArrayList<>();
        for (Long id : wishlist(s)) {
            try {
                list.add(watches.get(id));
            } catch (Exception ignored) {
            }
        }
        model.addAttribute("watches", list);
        return "wishlist";
    }
}
