package com.project.summoners_beta.web;

import com.project.summoners_beta.exceptions.ObjectNotFoundException;
import com.project.summoners_beta.model.dto.UserRoleChangeDTO;
import com.project.summoners_beta.service.UserRolesService;
import com.project.summoners_beta.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

@Controller
public class AdminPageController {

    private final UserRolesService userRolesService;

    public AdminPageController(UserRolesService userRolesService) {
        this.userRolesService = userRolesService;
    }

    @GetMapping("/admin-page")
    public String getAdminPage(@AuthenticationPrincipal User user, Model model) {



        return "admin-page";
    }

    @PostMapping("/admin-page")
    public String changeUserRoles(@AuthenticationPrincipal User user,
                                  UserRoleChangeDTO userRoleChangeDTO,
                                  RedirectAttributes redirectAttributes) {

        if (user.getUsername().equals(userRoleChangeDTO.getUsername())) {
            redirectAttributes.addFlashAttribute("preventChangeMsg",
                    "Can't change your own roles!");

            return "redirect:/admin-page";
        }

        if (userRoleChangeDTO.getRole() == null) {
            redirectAttributes.addFlashAttribute("selectRoleMsg", "Select a role!");

            return "redirect:/admin-page";
        }

        try {
            this.userRolesService.changeRole(userRoleChangeDTO);
        }
        catch (ObjectNotFoundException ex) {

            redirectAttributes.addFlashAttribute("noUserMsg",
                    "Could not find this User!");

            return "redirect:/admin-page";
        }

        redirectAttributes.addFlashAttribute("successMsg", "Role was changed successfully!");

        return "redirect:/admin-page";
    }
}
