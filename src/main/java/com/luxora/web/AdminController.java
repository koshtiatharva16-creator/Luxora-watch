
package com.luxora.web;

import com.luxora.model.AppUser;
import com.luxora.model.Watch;
import com.luxora.service.OrderService;
import com.luxora.service.UserService;
import com.luxora.service.WatchService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final WatchService watches;
    private final UserService users;
    private final OrderService orders;

    public AdminController(
            WatchService watches,
            UserService users,
            OrderService orders) {
        this.watches = watches;
        this.users = users;
        this.orders = orders;
    }

    private boolean isAdmin(HttpSession session) {
        Object o = session.getAttribute("currentUser");
        return o instanceof AppUser
                && "ADMIN".equals(((AppUser) o).getRole());
    }

    @GetMapping
    public String dashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login?admin";
        }

        model.addAttribute("watchCount", watches.count());
        model.addAttribute("userCount", users.count());
        model.addAttribute("orderCount", orders.count());
        model.addAttribute("watches", watches.all());
        model.addAttribute("orders", orders.all());

        return "admin/dashboard";
    }

    // INSERT WATCH PAGE
    @GetMapping("/watches/new")
    public String newWatch(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login?admin";
        }

        model.addAttribute("watch", new Watch());
        model.addAttribute("formTitle", "Add Watch");

        return "admin/watch-form";
    }

    // EDIT WATCH PAGE
    @GetMapping("/watches/{id}/edit")
    public String editWatch(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login?admin";
        }

        model.addAttribute("watch", watches.get(id));
        model.addAttribute("formTitle", "Edit Watch");

        return "admin/watch-form";
    }

    // INSERT AND UPDATE
    @PostMapping("/watches/save")
    public String saveWatch(
            @Valid @ModelAttribute("watch") Watch watch,
            BindingResult result,
            HttpSession session,
            Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login?admin";
        }

        if (result.hasErrors()) {
            model.addAttribute(
                    "formTitle",
                    watch.getId() == null ? "Add Watch" : "Edit Watch");

            return "admin/watch-form";
        }

        watches.save(watch);

        return "redirect:/admin?success";
    }

    // DELETE WATCH
    @PostMapping("/watches/{id}/delete")
    public String deleteWatch(
            @PathVariable Long id,
            HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/login?admin";
        }

        watches.delete(id);

        return "redirect:/admin";
    }
}
