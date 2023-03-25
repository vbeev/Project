package com.project.summoners_beta.web;

import com.project.summoners_beta.model.dto.CreateSummonDTO;
import com.project.summoners_beta.service.SummonService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SummonCreationController {

    private final SummonService summonService;

    public SummonCreationController(SummonService summonService) {
        this.summonService = summonService;
    }

    @GetMapping("/summons/creation")
    public String creation() {
        return "creation";
    }

    @PostMapping("/summons/creation")
    public String createSummon(@AuthenticationPrincipal User user, CreateSummonDTO createSummonDTO) {

        this.summonService.create(createSummonDTO, user.getUsername());

        return "redirect:/main-page";
    }

}
