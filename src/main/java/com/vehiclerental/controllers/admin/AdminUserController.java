// com.vehiclerental.controllers.admin.AdminUserController
package com.vehiclerental.controllers.admin;

import com.vehiclerental.models.User;
import com.vehiclerental.services.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) String q,
                       Model model) {

        Page<User> users = userService.search(q, PageRequest.of(page, size));
        model.addAttribute("users", users);
        model.addAttribute("query", q);
        model.addAttribute("pageTitle", "Manage Users");
        return "admin/users/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", User.Role.values());
        model.addAttribute("pageTitle", "Add New User");
        return "admin/users/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("user") User user,
                         BindingResult result,
                         @RequestParam(required = false) User.Role[] roles,
                         RedirectAttributes ra) {

        if (userService.usernameExists(user.getUsername())) {
            result.rejectValue("username", "error.user", "Username already exists");
        }
        if (userService.emailExists(user.getEmail())) {
            result.rejectValue("email", "error.user", "Email already exists");
        }
        if (result.hasErrors()) return "admin/users/form";

        userService.createByAdmin(user, roles);
        ra.addFlashAttribute("success", "User created successfully");
        return "redirect:/admin/users";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        // findById already throws if not found
        User u = userService.findById(id);
        model.addAttribute("user", u);
        model.addAttribute("allRoles", User.Role.values());
        model.addAttribute("pageTitle", "Edit User");
        return "admin/users/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("user") User form,
                         BindingResult result,
                         @RequestParam(required = false) User.Role[] roles,
                         RedirectAttributes ra) {

        if (result.hasErrors()) return "admin/users/form";
        userService.updateByAdmin(id, form, roles);
        ra.addFlashAttribute("success", "User updated");
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        userService.deleteByAdmin(id);
        ra.addFlashAttribute("success", "User deleted");
        return "redirect:/admin/users";
    }
}
