package com.example.q.pacemaker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by q on 2017-01-15.
 */

public class CommunityActivity extends Activity {
    private ArrayList<UserInfo> userList;
    private RecyclerView userRecyclerView;
    private RecyclerView.Adapter userAdapter;
    private RecyclerView.LayoutManager userLayoutManager;
    private FloatingActionButton enterChatButton;

    private String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        //TODO: pid 도 받아오기
        pid = "test1234";

        //TODO: 동참인원 정보 받아와서 userList에 채워넣기 username, 사진 url 필요
        userList = new ArrayList<>();
        for(int i = 0 ; i < 7 ; i++){
            userList.add(new UserInfo("한효주","", ""));
        }


        userRecyclerView = (RecyclerView) findViewById(R.id.user_recyclerview);
        userLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        userRecyclerView.setLayoutManager(userLayoutManager);
        userAdapter = new UserInfoAdapter(userList, getApplicationContext());
        userRecyclerView.setAdapter(userAdapter);

        enterChatButton = (FloatingActionButton) findViewById(R.id.enter_chat);
        enterChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatRoomActivity.class);
                intent.putExtra("pid",pid);
                startActivity(intent);
            }
        });

    }
}
