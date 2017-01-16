package com.example.q.pacemaker;

/**
 * Created by q on 2017-01-15.
 */

public class UserInfo {
    public String userName;
    public String url;
    public String token;

    public UserInfo(String userName, String url, String token){
        this.token = token;
        this.userName = userName;
        this.url = url;
    }
}
