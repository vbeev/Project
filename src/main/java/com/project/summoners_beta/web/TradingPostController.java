package com.project.summoners_beta.web;

import com.project.summoners_beta.exceptions.NotEnoughCoinsException;
import com.project.summoners_beta.model.dto.ConfirmOfferDTO;
import com.project.summoners_beta.model.dto.TradeOfferDTO;
import com.project.summoners_beta.model.enums.OfferType;
import com.project.summoners_beta.service.OfferService;
import com.project.summoners_beta.service.SummonService;
import com.project.summoners_beta.service.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TradingPostController {

    private final OfferService offerService;
    private final SummonService summonService;

    private final UserService userService;

    public TradingPostController(OfferService offerService, SummonService summonService, UserService userService) {
        this.offerService = offerService;
        this.summonService = summonService;
        this.userService = userService;
    }

    @GetMapping("/trading-post/offers/trade")
    public String tradingOffersPage(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("userSummons", this.summonService.getAllByUsername(user.getUsername()));

        model.addAttribute("offers",
                this.offerService.getAllByTypeAndByNotUser(OfferType.TRADE, user.getUsername()));

        return "trading-page";
    }

    @PostMapping(value = "/trading-post/offers/trade", params = "create")
    public String makeTradeOffers(@AuthenticationPrincipal User user, TradeOfferDTO tradeOfferDTO) {

        if (tradeOfferDTO.getSummonId() != null) {
            this.offerService.createOffer(tradeOfferDTO, OfferType.TRADE, user.getUsername());
        }

        return "redirect:/my-offers";
    }

    @PostMapping(value = "/trading-post/offers/trade", params = "offerId")
    public String acceptTradeOffers(@AuthenticationPrincipal User user,
                                    TradeOfferDTO tradeOfferDTO,
                                    ConfirmOfferDTO confirmOfferDTO,
                                    RedirectAttributes redirectAttributes) {

        if (tradeOfferDTO.getSummonId() != null) {
            this.offerService.acceptOffer(tradeOfferDTO, confirmOfferDTO, OfferType.TRADE, user.getUsername());

            return "redirect:/main-page";
        }

        redirectAttributes.addFlashAttribute("noTradeMsg","You can't make that trade!");

        return "redirect:/trading-post/offers/trade";
    }

    @GetMapping("/trading-post/offers/sell")
    public String sellOffersPage(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("userSummons", this.summonService.getAllByUsername(user.getUsername()));

        model.addAttribute("offers",
                this.offerService.getAllByTypeAndByNotUser(OfferType.SELL, user.getUsername()));

        model.addAttribute("coins", this.userService.getByUsername(user.getUsername()).getCoins());

        return "sales-page";
    }

    @PostMapping(value = "/trading-post/offers/sell", params = "create")
    public String makeSellOffers(@AuthenticationPrincipal User user,
                                 TradeOfferDTO tradeOfferDTO,
                                 RedirectAttributes redirectAttributes) {

        if (tradeOfferDTO.getSummonId() != null &&
                tradeOfferDTO.getPrice() != null && tradeOfferDTO.getPrice() > 0) {

            this.offerService.createOffer(tradeOfferDTO, OfferType.SELL, user.getUsername());

            return "redirect:/my-offers";
        }

        redirectAttributes.addFlashAttribute("enterPriceMsg", "Enter a valid price!");

        return "redirect:/trading-post/offers/sell";
    }

    @PostMapping(value = "/trading-post/offers/sell", params = "offerId")
    public String acceptSellOffers(@AuthenticationPrincipal User user,
                                   RedirectAttributes redirectAttributes,
                                   TradeOfferDTO tradeOfferDTO,
                                   ConfirmOfferDTO confirmOfferDTO) {

        try {
            this.offerService.acceptOffer(tradeOfferDTO, confirmOfferDTO, OfferType.SELL, user.getUsername());
        }
        catch (NotEnoughCoinsException ex) {
            redirectAttributes.addFlashAttribute("notEnoughCoinsMsg", ex.getMessage());

            return "redirect:/trading-post/offers/sell";
        }

        return "redirect:/main-page";
    }
}
