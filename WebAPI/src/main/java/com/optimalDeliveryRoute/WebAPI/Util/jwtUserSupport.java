package com.optimalDeliveryRoute.WebAPI.Util;

import com.optimalDeliveryRoute.WebAPI.Exceptions.APiExpiredJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Service
public class jwtUserSupport {
    private JwttUtil jwtUtil;

    @Autowired
    public jwtUserSupport(JwttUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String getUserNameInJwt(HttpServletRequest request) {
        var cookies =request.getCookies();

        String jwt=null;
        if(cookies!=null) {
             jwt = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(Cookies.JWT_TOKEN)).findFirst().map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("authorization is not valid"));
        }
        else {
            final String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null&& authorizationHeader.startsWith("Bearer ")){
                jwt =authorizationHeader.substring(7);
            }
        }
        try{
            return jwtUtil.extractUsername(jwt);
        }catch(ExpiredJwtException e){
            throw new APiExpiredJwtException(e);
        }
    }
}
