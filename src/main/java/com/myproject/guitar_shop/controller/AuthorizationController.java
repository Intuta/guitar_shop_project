package com.myproject.guitar_shop.controller;

import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.security.UserDetailsServiceImpl;
import com.myproject.guitar_shop.service.UserService;
import com.myproject.guitar_shop.utility.RequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AuthorizationController extends AbstractController {

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
        User currentUser = service.create(service.mapUser(userInfo));

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

}
