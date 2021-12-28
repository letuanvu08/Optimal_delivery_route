package com.optimalDeliveryRoute.WebAPI.Entity;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Data
@Table(name = "user")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @SequenceGenerator(
            name="user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private long id;
    @NotNull()
    private String username;
    @NotNull
    @Email
    private String email;
    @NotNull(message="Password is a required field")
    @Size(min=2, max=255, message="Password must be equal to or greater than 8 characters and less than 16 characters")
    private String password;
    private String phone;
    private String firstname;
    private String lastname;
    private String address;
    private String dateofbirth;
    private String transport;
}
