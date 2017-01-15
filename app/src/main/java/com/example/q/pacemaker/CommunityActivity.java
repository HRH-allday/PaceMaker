package com.example.q.pacemaker;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by q on 2017-01-15.
 */

public class CommunityActivity extends Activity {
    private ArrayList<UserInfo> userList;
    private RecyclerView userRecyclerView;
    private RecyclerView.Adapter userAdapter;
    private RecyclerView.LayoutManager userLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        //TODO: 동참인원 정보 받아와서 userList에 채워넣기 username, 사진 url 필요
        userList = new ArrayList<>();
        for(int i = 0 ; i < 7 ; i++){
            userList.add(new UserInfo("한효주",""));
        }


        userRecyclerView = (RecyclerView) findViewById(R.id.user_recyclerview);
        userLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        userRecyclerView.setLayoutManager(userLayoutManager);
        userAdapter = new UserInfoAdapter(userList, getApplicationContext());
        userRecyclerView.setAdapter(userAdapter);

    }
}
