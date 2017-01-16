package com.example.q.pacemaker;

/**
 * Created by q on 2017-01-15.
 */

public class ChatMessage {
    public String message;
    public UserInfo user;
    public int type;

    public ChatMessage (String msg, UserInfo info, int type){
        this.type = type;
        this.message = msg;
        this.user = info;
    }
}
