package com.optimalDeliveryRoute.WebAPI.Filters;

import com.optimalDeliveryRoute.WebAPI.Exceptions.APiExpiredJwtException;
import com.optimalDeliveryRoute.WebAPI.Service.UserDetailsServiceIml;
import com.optimalDeliveryRoute.WebAPI.Util.JwttUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    public JwtRequestFilter(UserDetailsServiceIml userDetailsServices, JwttUtil jwtUtil) {
        this.userDetailsServices = userDetailsServices;
        this.jwtUtil = jwtUtil;
    }

    private UserDetailsServiceIml userDetailsServices;
    private JwttUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String userName = null;
        String jwt = null;
        if (authorizationHeader != null&& authorizationHeader.startsWith("Bearer ")){
            jwt =authorizationHeader.substring(7);
            try{
            userName= jwtUtil.extractUsername(jwt);
            }catch(ExpiredJwtException e){
                throw new APiExpiredJwtException(e);
            }
        }
        if(userName != null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails =userDetailsServices.loadUserByUsername(userName);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        filterChain.doFilter(request,response);
    }
}
