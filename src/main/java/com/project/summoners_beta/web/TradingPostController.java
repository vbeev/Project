package com.project.summoners_beta.web;

import com.project.summoners_beta.model.dto.ConfirmOfferDTO;
import com.project.summoners_beta.model.dto.TradeOfferDTO;
import com.project.summoners_beta.model.enums.OfferType;
import com.project.summoners_beta.service.OfferService;
import com.project.summoners_beta.service.SummonService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TradingPostController {

    private final OfferService offerService;
    private final SummonService summonService;

    public TradingPostController(OfferService offerService, SummonService summonService) {
        this.offerService = offerService;
        this.summonService = summonService;
    }

    @GetMapping("/trading-post/offers/trade")
    public String tradingOffersPage(@AuthenticationPrincipal User user, Model model) {

     //   List<OfferDTO> offerDTOList = this.offerService.getAll();

   //     List<SummonDTO> summonDTOList = this.summonService.getAllByUsername(user.getUsername());

        model.addAttribute("userSummons", this.summonService.getAllByUsername(user.getUsername()));

        model.addAttribute("offers",
                this.offerService.getAllByTypeAndByNotUser(OfferType.TRADE, user.getUsername()));

       // model.addAttribute("offers", this.offerService.getAll());

        return "trading-page";
    }

    @PostMapping(value = "/trading-post/offers/trade", params = "create")
    public String makeTradeOffers(@AuthenticationPrincipal User user, TradeOfferDTO tradeOfferDTO) {

        if (tradeOfferDTO.getSummonId() != null) {
            this.offerService.createOffer(tradeOfferDTO, OfferType.TRADE, user.getUsername());
        }

        return "redirect:/trading-post/offers/trade";
    }

    @PostMapping(value = "/trading-post/offers/trade", params = "offerId")
    public String acceptTradeOffers(@AuthenticationPrincipal User user,
                               TradeOfferDTO tradeOfferDTO, ConfirmOfferDTO confirmOfferDTO) {

        if (tradeOfferDTO.getSummonId() != null) {
            this.offerService.acceptOffer(tradeOfferDTO, confirmOfferDTO, OfferType.TRADE, user.getUsername());
        }

        return "redirect:/trading-post/offers/trade";
    }

    @GetMapping("/trading-post/offers/sell")
    public String sellOffersPage(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("userSummons", this.summonService.getAllByUsername(user.getUsername()));

        model.addAttribute("offers",
                this.offerService.getAllByTypeAndByNotUser(OfferType.SELL, user.getUsername()));

        // model.addAttribute("offers", this.offerService.getAll());

        return "sales-page";
    }

    @PostMapping(value = "/trading-post/offers/sell", params = "create")
    public String makeSellOffers(@AuthenticationPrincipal User user, TradeOfferDTO tradeOfferDTO) {

        if (tradeOfferDTO.getSummonId() != null &&
                tradeOfferDTO.getPrice() != null && tradeOfferDTO.getPrice() > 0) {

            this.offerService.createOffer(tradeOfferDTO, OfferType.SELL, user.getUsername());
        }

        return "redirect:/trading-post/offers/sell";
    }

    @PostMapping(value = "/trading-post/offers/sell", params = "offerId")
    public String acceptSellOffers(@AuthenticationPrincipal User user,
                               TradeOfferDTO tradeOfferDTO, ConfirmOfferDTO confirmOfferDTO) {


        this.offerService.acceptOffer(tradeOfferDTO, confirmOfferDTO, OfferType.SELL, user.getUsername());

        return "redirect:/trading-post/offers/sell";
    }
}
