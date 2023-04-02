package com.project.summoners_beta.web;

import com.project.summoners_beta.model.dto.CardBattleDTO;
import com.project.summoners_beta.model.dto.SummonDTO;
import com.project.summoners_beta.service.BattleService;
import com.project.summoners_beta.service.SummonService;
import com.project.summoners_beta.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
                               Model model, @RequestParam(name = "cardId") Long cardId) {

        model.addAttribute("userCard", this.summonService.getById(cardId));
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
    public String postArenaPage(@AuthenticationPrincipal User user, Model model, CardBattleDTO cardBattleDTO) {

        SummonDTO userCard = this.summonService.getById(cardBattleDTO.getUserCardId());

        SummonDTO enemyCard = this.summonService.getById(cardBattleDTO.getEnemyCardId());

        boolean victory = this.battleService.battle(userCard, enemyCard);

        if (victory) {
            this.userService.grantReward(user.getUsername());
            return "redirect:/arena/victory";
        }

        return "redirect:/arena/defeat";
    }

}
