package com.project.summoners_beta.web;

import com.project.summoners_beta.model.dto.TradeOfferDTO;
import com.project.summoners_beta.model.enums.OfferType;
import com.project.summoners_beta.service.SummonService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainPageController {

    private final SummonService summonService;

    public MainPageController(SummonService summonService) {
        this.summonService = summonService;
    }

    @GetMapping("/main-page")
    public String getMainPage(@AuthenticationPrincipal User user, Model model) {


        model.addAttribute("cards",
               this.summonService.getAllByUsername(user.getUsername()));

        // model.addAttribute("offers", this.offerService.getAll());

        return "main-page";
    }

    /*@PostMapping(value = "/main-page", params = "cardId")
    public String postCardId(*//*@AuthenticationPrincipal User user, TradeOfferDTO tradeOfferDTO*//*) {


        return "redirect:/arena";
    }*/
}
