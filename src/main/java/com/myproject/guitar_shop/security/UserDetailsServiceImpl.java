package com.myproject.guitar_shop.security;

import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.exception.NonExistentUserException;
import com.myproject.guitar_shop.repository.UserRepository;
import com.myproject.guitar_shop.utility.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository repository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository repository) {
        super();
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = repository.findByEmail(email.toLowerCase()).orElseThrow(() -> new NonExistentUserException(ErrorMessages.USER_NOT_FOUND));
        return new UserPrincipal(user);
    }

    public void setUsernamePasswordAuthenticationToken(User user) {
        UserDetails userDetails = loadUserByUsername(user.getEmail());
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), userDetails.getAuthorities());
        if (token.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(token);
        }
    }

}
