package com.optimalDeliveryRoute.WebAPI.model.User.Request;

public class UserChangePasswordRequest {

    public String getOldpassword() {
        return oldpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    private String oldpassword;
    private String newpassword;

    public UserChangePasswordRequest(String oldPassword, String newPassword) {
        this.oldpassword = oldPassword;
        this.newpassword = newPassword;
    }
}
