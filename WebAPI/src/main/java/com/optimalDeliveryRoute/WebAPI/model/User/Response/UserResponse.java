package com.optimalDeliveryRoute.WebAPI.model.User.Response;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String username;
    private String email;
    private String phone;
    private String firstname;
    private String lastname;
    private String address;
    private String dateOfBirth;
    private String transport;
}
