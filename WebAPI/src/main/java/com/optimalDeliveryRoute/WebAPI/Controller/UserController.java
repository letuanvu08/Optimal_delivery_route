package com.optimalDeliveryRoute.WebAPI.Controller;

import com.optimalDeliveryRoute.WebAPI.Exceptions.ApiRequestException;
import com.optimalDeliveryRoute.WebAPI.Service.UserDetailsServiceIml;
import com.optimalDeliveryRoute.WebAPI.Service.UserService;
import com.optimalDeliveryRoute.WebAPI.Util.JwttUtil;
import com.optimalDeliveryRoute.WebAPI.Util.jwtUserSupport;
import com.optimalDeliveryRoute.WebAPI.model.Authentication.AuthenticationRequest;
import com.optimalDeliveryRoute.WebAPI.model.Authentication.AuthenticationResponse;
import com.optimalDeliveryRoute.WebAPI.model.User.Request.UserChangePasswordRequest;
import com.optimalDeliveryRoute.WebAPI.model.User.Response.UserResponse;
import com.optimalDeliveryRoute.WebAPI.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/users")
public class UserController {


    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceIml userDetailsService;
    private final JwttUtil jwttUtil;
    private final jwtUserSupport jwtUserSupport;

    @Autowired
    public UserController(AuthenticationManager authenticationManager,
                          UserDetailsServiceIml userDetailsService,
                          JwttUtil jwttUtil,
                          UserService userService,
                          jwtUserSupport jwtUserSupport) {
        this.userService =userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwttUtil = jwttUtil;
        this.jwtUserSupport=jwtUserSupport;
    }

    @GetMapping("/get")
    public ResponseEntity<UserResponse> GetUser(HttpServletRequest request) {
        var username = jwtUserSupport.getUserNameInJwt(request);
        return ResponseEntity.ok(userService.GetUserResponseByName(username));
    }

    @PostMapping("/register")

    public ResponseEntity<Boolean> register(@Valid @RequestBody User user) {
        userService.RegisterUser(user);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@Valid @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new ApiRequestException("Incorrect username of password", e);
        }
        final UserDetails userdetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwttUtil.generateToken(userdetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
    @PutMapping("/changepassword")
    public ResponseEntity<Boolean> changePassword(HttpServletRequest request,@Valid @RequestBody UserChangePasswordRequest userChangePasswordRequest ){
        var username = jwtUserSupport.getUserNameInJwt(request);
        return ResponseEntity.ok(userService.ChangePassWord(username,userChangePasswordRequest));
    }
    @PutMapping("/update")
    public ResponseEntity<Boolean> UpdateUser(HttpServletRequest request,@Valid @RequestBody UserResponse userUpdate ){
        var username = jwtUserSupport.getUserNameInJwt(request);
        return ResponseEntity.ok(userService.UpdateUser(username,userUpdate));
    }
    @DeleteMapping()
    public ResponseEntity<Boolean> DeleteUser(HttpServletRequest request){
        var username = jwtUserSupport.getUserNameInJwt(request);
        return ResponseEntity.ok(userService.DeleteUser(username));

    }

}





