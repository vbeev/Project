package com.project.summoners_beta.web;

import com.project.summoners_beta.model.dto.CardForBattleDTO;
import com.project.summoners_beta.service.SummonService;
import com.project.summoners_beta.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainPageController {

    private final SummonService summonService;

    private final UserService userService;

    public MainPageController(SummonService summonService, UserService userService) {
        this.summonService = summonService;
        this.userService = userService;
    }

    @GetMapping("/main-page")
    public String getMainPage(@AuthenticationPrincipal User user, Model model) {


        model.addAttribute("cards",
               this.summonService.getAllByUsername(user.getUsername()));

        model.addAttribute("coins", this.userService.getByUsername(user.getUsername()).getCoins());

        return "main-page";
    }

    @PostMapping("/main-page")
    public String postSelectedCard(CardForBattleDTO cardForBattleDTO, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("cardForBattleDTO", cardForBattleDTO);

        return "redirect:/arena";
    }
}
