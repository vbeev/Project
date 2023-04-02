package com.project.summoners_beta.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorPageController {

    @GetMapping("/error-page")
    public ModelAndView getErrorPage(ModelAndView modelAndView) {

        modelAndView.setViewName("error-new");
        modelAndView.addObject("er-msg", "some text");

        return modelAndView;
    }
}
