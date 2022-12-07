package com.myproject.guitar_shop.controller;

import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.security.UserDetailsServiceImpl;
import com.myproject.guitar_shop.service.UserService;
import com.myproject.guitar_shop.utility.CurrentUserProvider;
import com.myproject.guitar_shop.utility.RequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
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
        User currentUser = service.save(service.mapUser(userInfo));

        UserDetails userDetails = userDetailsService.loadUserByUsername(currentUser.getEmail());
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, currentUser.getPassword(), userDetails.getAuthorities());

        if (token.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        return "home";
    }

    @GetMapping("/logout")
    public String logout() {
        return "home";
    }


    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/account")
    public String account(Model model) {
        model.addAttribute("user", CurrentUserProvider.getCurrentUser()); 
        return "home";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/updateUser/{user_id}")
    public String updateUSer(@PathVariable String user_id, @RequestParam Map<String, String> params, Model model) {
        User currentUser = service.save(service.update(params, Integer.parseInt(user_id)));

        UserDetails userDetails = userDetailsService.loadUserByUsername(currentUser.getEmail());
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, currentUser.getPassword(), userDetails.getAuthorities());

        if (token.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        model.addAttribute("user",currentUser);
        return "home";
    }

}
