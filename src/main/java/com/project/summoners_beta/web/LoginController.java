package com.project.summoners_beta.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @GetMapping("/users/login")
    public String login() {
        return "auth-login";
    }

    @PostMapping("/users/login-error")
    public String onFailedLogin(@ModelAttribute ("username") String username, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("username", username);
        redirectAttributes.addFlashAttribute("bad_credentials", true);

        return "redirect:/users/login";
    }
}
