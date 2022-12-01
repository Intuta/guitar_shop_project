package com.myproject.guitar_shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController extends AbstractController {

    @RequestMapping(value = {"", "/home"})
    public String home(Model model) {
        model.addAttribute("user", this.getCurrentUser());
        return "home";
    }

}
