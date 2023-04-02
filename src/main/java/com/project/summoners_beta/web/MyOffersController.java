package com.project.summoners_beta.web;

import com.project.summoners_beta.exceptions.UserAlreadyExistsException;
import com.project.summoners_beta.model.dto.ConfirmOfferDTO;
import com.project.summoners_beta.model.enums.OfferType;
import com.project.summoners_beta.service.OfferService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MyOffersController {

    private final OfferService offerService;

    public MyOffersController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping("/my-offers")
    public String myOffersPage(@AuthenticationPrincipal User user, Model model) {


        model.addAttribute("offers",
                this.offerService.getAllOffersByUsername(user.getUsername()));

        return "my-offers-page";
    }

    @DeleteMapping("/my-offers")
    public String deleteMyOffers(@AuthenticationPrincipal User user, ConfirmOfferDTO confirmOfferDTO) {

        this.offerService.removeOffer(confirmOfferDTO, user.getUsername());

        return "redirect:/my-offers";
    }

}
