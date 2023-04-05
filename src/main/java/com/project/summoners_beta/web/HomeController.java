package com.project.summoners_beta.web;

import com.project.summoners_beta.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  private final UserService userService;

  public HomeController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/")
  public String home(@AuthenticationPrincipal User user, Model model) {

    if (user != null) {
      model.addAttribute("username", user.getUsername());
      model.addAttribute("coins", this.userService.getByUsername(user.getUsername()).getCoins());
    }

    return "index";
  }
}
