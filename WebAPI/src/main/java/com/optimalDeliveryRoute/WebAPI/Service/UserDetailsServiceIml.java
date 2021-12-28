package com.optimalDeliveryRoute.WebAPI.Service;

import com.optimalDeliveryRoute.WebAPI.Exceptions.ApiRequestException;
import com.optimalDeliveryRoute.WebAPI.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceIml implements UserDetailsService {
    private final UserRepository userRepository;
    @Autowired
    public UserDetailsServiceIml(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findUserByName(username).orElseThrow(() -> new ApiRequestException("User name not be found"));
        return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}
