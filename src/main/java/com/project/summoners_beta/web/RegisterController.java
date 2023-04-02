package com.project.summoners_beta.web;

import com.project.summoners_beta.exceptions.UserAlreadyExistsException;
import com.project.summoners_beta.model.dto.UserRegisterDTO;
import com.project.summoners_beta.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    private final UserService userService;

    private final SecurityContextRepository securityContextRepository;

    public RegisterController(UserService userService, SecurityContextRepository securityContextRepository) {
        this.userService = userService;
        this.securityContextRepository = securityContextRepository;
    }

    @GetMapping("/users/register")
    public String register() {
        return "register-page";
    }

    @PostMapping("/users/register")
    public String registerUser(@Valid @ModelAttribute(name = "userRegisterDTO") UserRegisterDTO userRegisterDTO,
                               BindingResult bindingResult,
                               HttpServletRequest request,
                               HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            return "register-page";
        }


        this.userService.registerUser(userRegisterDTO, successfulAuth -> {
            SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();

            SecurityContext context = strategy.createEmptyContext();
            context.setAuthentication(successfulAuth);

            strategy.setContext(context);

            securityContextRepository.saveContext(context, request, response);
        });


        return "redirect:/";
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ModelAndView  onUserExists(UserAlreadyExistsException ex) {
        ModelAndView modelAndView =  new ModelAndView("register-page");
        modelAndView.addObject("errorExistsMsg", ex.getMessage());
        modelAndView.addObject("userRegisterDTO", new UserRegisterDTO());
        return modelAndView;
    }

    @ModelAttribute(name = "userRegisterDTO")
    public UserRegisterDTO userRegisterDTO() {
        return new UserRegisterDTO();
    }
}
