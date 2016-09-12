package com.example.smartprotector.Bean;

/**
 * Created by Huhu on 8/16/15.
 * 储存登录用户的基本信息。
 */
public class CurrentUser {
    //用户登录手机号
    private String user_phone;
    //用户昵称
    private String user_nickname;
    //头像链接
    private String image_path;


    public CurrentUser(String user_phone, String user_nickname, String image_path) {
        this.user_phone = user_phone;
        this.user_nickname = user_nickname;
        this.image_path = image_path;

    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }


}
