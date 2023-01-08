package com.myproject.guitar_shop.controller;

import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.exception.IncorrectPasswordException;
import com.myproject.guitar_shop.exception.NonExistentUserException;
import com.myproject.guitar_shop.exception.NonUniqueEmailException;
import com.myproject.guitar_shop.security.UserDetailsServiceImpl;
import com.myproject.guitar_shop.service.UserService;
import com.myproject.guitar_shop.utility.CurrentUserProvider;
import com.myproject.guitar_shop.utility.ErrorMessages;
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
    public String addNewUser(@RequestParam Map<String, String> params) {
        User currentUser = userService.createUser(params);
        userDetailsService.setUsernamePasswordAuthenticationToken(currentUser);
        return "home";
    }

    @GetMapping("/logout")
    public String logout() {
        return "home";
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMINISTRATOR')")
    @GetMapping("/account")
    public String getAccount(Model model) {
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
    public String changeUserPasswordFormRedirect() {
        return "password_change";
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

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/update_user_role/{user_id}")
    public String updateUserRole(@RequestParam Map<String, String> params, Model model, @PathVariable String user_id) {
        User currentUser = userService.getById(Integer.parseInt(user_id));
        currentUser = userService.save(userService.update(params, currentUser.getId()));

        model.addAttribute("users", userService.getAllUsersByAttribute(currentUser.getEmail()));
        model.addAttribute("users_search_form", true);
        return "home";
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/manage_users_form")
    public String usersFormRedirect(Model model) {
        model.addAttribute("users_search_form", true);
        return "home";
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/users")
    public String getUsers(@RequestParam(value = "attribute") String attribute, Model model) {
        model.addAttribute("users", userService.getAllUsersByAttribute(attribute));
        model.addAttribute("users_search_form", true);
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
