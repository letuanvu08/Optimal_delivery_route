package com.optimalDeliveryRoute.WebAPI.Service;

import com.optimalDeliveryRoute.WebAPI.Exceptions.ApiRequestException;
import com.optimalDeliveryRoute.WebAPI.Repository.UserRepository;
import com.optimalDeliveryRoute.WebAPI.model.User.Request.UserChangePasswordRequest;
import com.optimalDeliveryRoute.WebAPI.Entity.User;
import com.optimalDeliveryRoute.WebAPI.model.User.Response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void RegisterUser(User user) {
        if (this.userRepository.findUserByName(user.getUsername()).isPresent()) {
            throw new ApiRequestException(" username was exist");
        }
        this.userRepository.save(user);
    }

    public boolean ChangePassWord(String userName, UserChangePasswordRequest userChangePasswordRequest) {
        var user= this.userRepository.findUserByName(userName).orElseThrow(() -> new ApiRequestException("The userName is not found"));
        if(user.getPassword().equals(userChangePasswordRequest.getOldpassword())){
         this.userRepository.ChangePassword(userName, userChangePasswordRequest.getNewpassword());
         return true;
        }
        return false;
    }

    public User GetUserByName(String userName) {
        return this.userRepository.findUserByName(userName)
                .orElseThrow(() ->  new ApiRequestException("The userName is not found"));
    }

    public User GetUserById(long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() ->  new ApiRequestException("The id is not found"));
    }

    public boolean DeleteUser(String username) {
        var user= this.userRepository.findUserByName(username).orElseThrow(() -> new ApiRequestException("The userName is not found"));
        this.userRepository.delete(user);
        return true;
    }

    public UserResponse GetUserResponseByName(String username) {
        var user = this.GetUserByName(username);
        return new UserResponse(
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getFirstname(),
                user.getLastname(),
                user.getAddress(),
                user.getDateofbirth(),
                user.getTransport());
    }

    public Boolean UpdateUser(String username,UserResponse userUpdate) {
        var user= this.userRepository.findUserByName(username).orElseThrow(() -> new ApiRequestException("The userName is not found"));
        user.setFirstname(userUpdate.getFirstname());
        user.setLastname(userUpdate.getLastname());
        user.setEmail(userUpdate.getEmail());
        user.setAddress(userUpdate.getAddress());
        user.setPhone(userUpdate.getPhone());
        user.setDateofbirth(userUpdate.getDateOfBirth());
        user.setTransport(userUpdate.getTransport());
        this.userRepository.save(user);
        return true;
    }
}
