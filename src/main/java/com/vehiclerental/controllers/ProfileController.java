// com.vehiclerental.controllers.ProfileController
package com.vehiclerental.controllers;

import com.vehiclerental.models.User;
import com.vehiclerental.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    public ProfileController(UserService userService) { this.userService = userService; }

    // GET /profile
    @GetMapping
    public String view(Authentication auth, Model model) {
        String username = auth.getName();
        User me = userService.findByUsername(username);
        model.addAttribute("user", me);
        model.addAttribute("pageTitle", "My Profile");
        return "profile/view"; // -> src/main/resources/templates/profile/view.html
    }

    // GET /profile/edit
    @GetMapping("/edit")
    public String editForm(Authentication auth, Model model) {
        String username = auth.getName();
        User me = userService.findByUsername(username);
        model.addAttribute("user", me);
        model.addAttribute("pageTitle", "Edit Profile");
        return "profile/edit"; // -> src/main/resources/templates/profile/edit.html
    }

    // POST /profile/edit
    @PostMapping("/edit")
    public String update(Authentication auth,
                         @Valid @ModelAttribute("user") User form,
                         BindingResult result,
                         RedirectAttributes ra,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Profile");
            return "profile/edit";
        }
        String username = auth.getName();
        userService.updateOwn(username, form);
        ra.addFlashAttribute("success", "Profile updated");
        return "redirect:/profile";
    }

    // POST /profile/delete  (optional feature)
    @PostMapping("/delete")
    public String delete(Authentication auth, RedirectAttributes ra) {
        String username = auth.getName();
        userService.deleteOwn(username);
        ra.addFlashAttribute("success", "Your account has been deleted.");
        // Usually you'd also log out the session; redirect to login or home.
        return "redirect:/logout";
    }
}
