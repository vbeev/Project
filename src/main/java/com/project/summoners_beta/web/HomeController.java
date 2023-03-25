package com.project.summoners_beta.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

/*  @GetMapping("/")
  public String home(@AuthenticationPrincipal AppUserDetails appUserDetails, Model model) {

    if (appUserDetails != null) {
      model.addAttribute("fullName", appUserDetails.getFullName());
      model.addAttribute("country", appUserDetails.getCountry());
    }

    return "index";
  }*/

  @GetMapping("/")
  public String home(@AuthenticationPrincipal User user, Model model) {

    if (user != null) {
      model.addAttribute("username", user.getUsername());
    }

    return "index";
  }

  @GetMapping("/pages/all")
  public String all() {
    return "all";
  }

  @GetMapping("/pages/admins")
  public String admins() {
    return "admins";
  }

  @GetMapping("/pages/moderators")
  public String moderators() {
    return "moderators";
  }
}
