package com.myproject.guitar_shop.controller;

import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.exception.IncorrectPasswordException;
import com.myproject.guitar_shop.exception.NonExistentUserException;
import com.myproject.guitar_shop.exception.NonUniqueEmailException;
import com.myproject.guitar_shop.security.UserDetailsServiceImpl;
import com.myproject.guitar_shop.service.UserService;
import com.myproject.guitar_shop.utility.CurrentUserProvider;
import com.myproject.guitar_shop.utility.ErrorMessages;
import com.myproject.guitar_shop.utility.RequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;

    @GetMapping("/log_in")
    public String log() {
        return "log_in";
    }

    @GetMapping("/registration")
    public String register() {
        return "registration";
    }

    @PostMapping("/register")
    public String addUser(@RequestBody String body) {
        Map<String, String> userInfo = RequestMapper.mapRequestBody(body);
        User currentUser = userService.mapUser(userInfo);
        userDetailsService.setUsernamePasswordAuthenticationToken(currentUser);

        return "home";
    }

    @GetMapping("/logout")
    public String logout() {
        return "home";
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMINISTRATOR')")
    @GetMapping("/account")
    public String account(Model model) {
        model.addAttribute("user", CurrentUserProvider.getCurrentUser());
        return "home";
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMINISTRATOR')")
    @GetMapping("/update_user/{user_id}")
    public String updateUser(@PathVariable String user_id, @RequestParam Map<String, String> params, Model model) {
        User currentUser = userService.save(userService.update(params, Integer.parseInt(user_id)));
        userDetailsService.setUsernamePasswordAuthenticationToken(currentUser);

        model.addAttribute("user", currentUser);
        return "home";
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMINISTRATOR')")
    @GetMapping("/password_change")
    public String passwordChange() {
        return "passwordChange";
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMINISTRATOR')")
    @PostMapping("/update_user")
    public String updateUserPassword(@RequestParam Map<String, String> params, Model model) {
        User currentUser = CurrentUserProvider.getCurrentUser();
        if (currentUser != null) {
            currentUser = userService.save(userService.update(params, currentUser.getId()));
            userDetailsService.setUsernamePasswordAuthenticationToken(currentUser);
            model.addAttribute("user", currentUser);
        }
        return "home";
    }

    @GetMapping("/access_denied")
    public String accessDenied(Model model) {
        model.addAttribute("errorMessage", ErrorMessages.ACCESS_DENIED);
        return "error";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IncorrectPasswordException.class, NonExistentUserException.class, NonUniqueEmailException.class})
    public String handleException(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }
}
