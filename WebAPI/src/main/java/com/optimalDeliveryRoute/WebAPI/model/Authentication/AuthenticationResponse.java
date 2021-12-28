package com.optimalDeliveryRoute.WebAPI.model.Authentication;

public class AuthenticationResponse {
    private final String jwttoken;

    public AuthenticationResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    public String getJwtToken() {
        return jwttoken;
    }
}
