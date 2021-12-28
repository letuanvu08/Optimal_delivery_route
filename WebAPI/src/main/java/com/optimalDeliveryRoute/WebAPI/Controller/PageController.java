package com.optimalDeliveryRoute.WebAPI.Controller;

import com.optimalDeliveryRoute.WebAPI.Service.UserDetailsServiceIml;
import com.optimalDeliveryRoute.WebAPI.Util.Cookies;
import com.optimalDeliveryRoute.WebAPI.Util.JwttUtil;
import com.optimalDeliveryRoute.WebAPI.Util.jwtUserSupport;
import com.optimalDeliveryRoute.WebAPI.model.Authentication.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class PageController {
    @Autowired
    private jwtUserSupport jwtUserSupport;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceIml userDetailsService;
    @Autowired
    private JwttUtil jwttUtil;

    @GetMapping("/home")
    public String showHomePage(){
        return "home";
    }

    @GetMapping("/history")
    public String show(){
        return "history";
    }

    @GetMapping("/profile")
    public String GetTrips(){
        return "profile";
    }

    @PostMapping("/login")
    public String login (@Valid AuthenticationRequest authenticationRequest, HttpServletResponse res) {
        System.out.println("Running");
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
            final UserDetails userdetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            Cookie cookie = new Cookie(Cookies.JWT_TOKEN, jwttUtil.generateToken(userdetails));
            cookie.setPath("/");
            cookie.setMaxAge(Integer.MAX_VALUE);
            res.addCookie(cookie);
        } catch (Exception e) {
            return "Incorrect username of password";
        }

        return "redirect:/home";
    }
    @GetMapping("/logout_user")
    public String logout (HttpServletResponse res) {
        Cookie cookie = new Cookie(Cookies.JWT_TOKEN, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        res.addCookie(cookie);

        return "redirect:/";
    }
    @GetMapping("/mymap")
    public String showMap (HttpServletRequest request) {
        jwtUserSupport.getUserNameInJwt(request);
        return "mymap";
    }
}
