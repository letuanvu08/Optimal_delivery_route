package com.optimalDeliveryRoute.WebAPI.model.Authentication;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AuthenticationRequest {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    @NotNull(message=" username cannot be missing or empty")
    private String username;
    @NotNull(message="Password is a required field")
    @Size(min=2, max=255, message="Password must be equal to or greater than 8 characters and less than 16 characters")
    private String password;
}
