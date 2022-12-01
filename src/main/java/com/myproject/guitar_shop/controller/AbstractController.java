package com.myproject.guitar_shop.controller;

import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;


@Controller
public class AbstractController {

    /**
     * The method contains general logic for transferring current User object
     * if it presents or null in the otherwise
     */
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserPrincipal) {
            UserPrincipal userPrincipal = ((UserPrincipal) principal);
            return userPrincipal.getUser();
        }
        else return null;
    }
}
