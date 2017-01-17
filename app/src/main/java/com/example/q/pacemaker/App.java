package com.example.q.pacemaker;

import android.app.Application;
import android.graphics.Color;
import android.graphics.Typeface;

public class App extends Application{

    // User Parameters
    public static String token;

    // Server Parameters
    public static final String server_ip = "52.78.200.87";
    public static final String server_port = "3000";
    public static final String server_protocol = "http";
    public static final String server_url = server_protocol + "://" + server_ip + ":" + server_port;

    public static final String routing_user_login = "/user_login";
    public static final String routing_user_register = "/user_register";
    public static final String routing_new_goal_register = "/new_goal_register";
    public static final String routing_main = "/get_todo";
    public static final String routing_user_info = "/user_info";
    public static final String routing_goal_clone_info = "/goal_clone_info";
    public static final String routing_goal_info = "/goal_info";
    public static final String routing_goal_list = "/goal_list";
    public static final String routing_add_todo = "/add_todo";
    public static final String routing_add_routine = "/add_routine";
    public static final String routing_add_memo = "/add_memo";
    public static final String routing_user_follow = "/user_follow";
    public static final String routing_get_chatroom = "/get_chatroom";
    public static final String routing_new_chat = "/new_chat";
    public static final String routing_check_register = "/check_register";
    public static final String routing_get_clone_list = "/get_clone_list";
    public static final String routing_update_clone_location = "/update_clone_location";

    public static final String JSONcontentsType = "application/json";

    // Fonts
    public static Typeface NanumBarunGothic;
    public static Typeface NanumBarunGothicBold;
    public static Typeface NanumBarunGothicLight;
    public static Typeface NanumBarunGothicUltraLight;
    public static Typeface NanumBarunpenBold;
    public static Typeface NanumBarunpenRegular;
    public static Typeface NanumGothic;
    public static Typeface NanumPen;

    // Colors
    public static final int COLOR_RED_1 = Color.parseColor("#f0a8a8");
    public static final int COLOR_RED_2 = Color.parseColor("#ff8b8b");
    public static final int COLOR_RED_3 = Color.parseColor("#f66b6b");
    public static final int COLOR_RED_4 = Color.parseColor("#ff4a4a");
    public static final int COLOR_RED_5 = Color.parseColor("#ff1f1f");

    @Override
    public void onCreate() {
        // Get Fonts
        NanumBarunGothic = Typeface.createFromAsset(getAssets(), "fonts/NanumBarunGothic.ttf");
        NanumBarunGothicBold = Typeface.createFromAsset(getAssets(), "fonts/NanumBarunGothicBold.ttf");
        NanumBarunGothicLight = Typeface.createFromAsset(getAssets(), "fonts/NanumBarunGothicLight.ttf");
        NanumBarunGothicUltraLight = Typeface.createFromAsset(getAssets(), "fonts/NanumBarunGothicUltraLight.ttf");
        NanumBarunpenBold = Typeface.createFromAsset(getAssets(), "fonts/NanumBarunpenB.ttf");
        NanumBarunpenRegular = Typeface.createFromAsset(getAssets(), "fonts/NanumBarunpenR.ttf");
        NanumGothic = Typeface.createFromAsset(getAssets(), "fonts/NanumGothic.ttf");
        NanumPen = Typeface.createFromAsset(getAssets(), "fonts/NanumPen.ttf");

        super.onCreate();
    }
}
