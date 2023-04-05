package com.project.summoners_beta.web;

import com.project.summoners_beta.exceptions.NotEnoughCoinsException;
import com.project.summoners_beta.model.dto.SummonCreationDTO;
import com.project.summoners_beta.service.SummonService;
import com.project.summoners_beta.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SummonCreationController {

    private final SummonService summonService;
    private final UserService userService;

    public SummonCreationController(SummonService summonService, UserService userService) {
        this.summonService = summonService;
        this.userService = userService;
    }

    @GetMapping("/summons/creation")
    public String creation(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("coins", this.userService.getByUsername(user.getUsername()).getCoins());

        return "create-page";
    }

    @PostMapping("/summons/creation")
    public String createSummon(@AuthenticationPrincipal User user,
                               @Valid @ModelAttribute(name = "summonCreationDTO") SummonCreationDTO summonCreationDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("nameLengthMsg",
                    "Name must be between 3 and 8 characters");

            return "redirect:/summons/creation";
        }

        try {
            this.summonService.create(summonCreationDTO, user.getUsername());
        }
        catch (NotEnoughCoinsException ex) {

            redirectAttributes.addFlashAttribute("notEnoughCoinsMsg", ex.getMessage());

            return "redirect:/summons/creation";
        }


        return "redirect:/roster";
    }

    @ModelAttribute(name = "summonCreationDTO")
    public SummonCreationDTO summonCreationDTO() {
        return new SummonCreationDTO();
    }
}


