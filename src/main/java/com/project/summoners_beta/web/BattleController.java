package com.project.summoners_beta.web;

import com.project.summoners_beta.exceptions.ObjectNotFoundException;
import com.project.summoners_beta.model.dto.BattleDTO;
import com.project.summoners_beta.model.dto.CardForBattleDTO;
import com.project.summoners_beta.model.dto.SummonDTO;
import com.project.summoners_beta.service.BattleService;
import com.project.summoners_beta.service.SummonService;
import com.project.summoners_beta.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BattleController {

    private final SummonService summonService;
    private final BattleService battleService;

    private final UserService userService;

    public BattleController(SummonService summonService,
                            BattleService battleService, UserService userService) {
        this.summonService = summonService;
        this.battleService = battleService;
        this.userService = userService;
    }

    @GetMapping("/arena")
    public String getArenaPage(@AuthenticationPrincipal User user,
                               Model model,
                               @ModelAttribute(name = "cardForBattleDTO") CardForBattleDTO cardForBattleDTO) {

        if (cardForBattleDTO.getCardId() == null) {
            throw new ObjectNotFoundException("A problem with fetching your card!");
        }

        model.addAttribute("userCard", this.summonService.getById(cardForBattleDTO.getCardId()));
        model.addAttribute("enemyCard",  this.summonService.getRandomByNotUser(user.getUsername()));

        return "battle-page";
    }

    @GetMapping("/arena/victory")
    public String getVictoryPage() {

        return "victory-page";
    }

    @GetMapping("/arena/defeat")
    public String getDefeatPage() {

        return "defeat-page";
    }

    @PostMapping("/arena")
    public String postArenaPage(@AuthenticationPrincipal User user, Model model, BattleDTO battleDTO) {

        SummonDTO userCard = this.summonService.getById(battleDTO.getUserCardId());

        SummonDTO enemyCard = this.summonService.getById(battleDTO.getEnemyCardId());

        boolean victory = this.battleService.battle(userCard, enemyCard);

        if (victory) {
            this.userService.grantReward(user.getUsername());
            return "redirect:/arena/victory";
        }

        return "redirect:/arena/defeat";
    }

}
