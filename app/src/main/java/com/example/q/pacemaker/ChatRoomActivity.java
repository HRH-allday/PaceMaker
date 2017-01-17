package com.example.q.pacemaker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;

import com.example.q.pacemaker.Adapters.ChatRoomListAdapter;
import com.example.q.pacemaker.Utilities.RoundedImageView;
import com.example.q.pacemaker.Utilities.SendJSON;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.q.pacemaker.MainActivity.cidList;
import static com.example.q.pacemaker.MainActivity.myUserInfo;
import static com.example.q.pacemaker.MainActivity.titleList;

public class ChatRoomActivity extends Activity {

    private FloatingActionButton addChatButton;
    private ChatCreateDialog chatCreateDialog;

    private RecyclerView recyclerViewChatRoom;
    private StaggeredGridLayoutManager staggeredGridLayoutManagerChatRoom;
    private ChatRoomListAdapter chatRoomListAdapter;

    private String token;
    private String photoUrl;
    private String pid;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        token = FirebaseInstanceId.getInstance().getToken();

        // TODO : setup pid
        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");

        ArrayList<String> titleLists = new ArrayList<>();
        ArrayList<String> cidLists = new ArrayList<>();


        try {
            JSONObject req = new JSONObject();
            req.put("token", token);

            JSONObject res = new SendJSON(App.server_url + App.routing_user_info, req.toString(), App.JSONcontentsType).execute().get();
            if (res != null && res.has("result") && res.getString("result").equals("success")) {
                JSONObject userData = res.getJSONObject("user");
                Log.i("userData", userData.toString());
                JSONArray titles = userData.getJSONArray("goals_title");
                JSONArray cids = userData.getJSONArray("goals_id");
                for(int i = 0; i < titles.length() ; i++){
                    titleLists.add(titles.getString(i));
                    cidLists.add(cids.getString(i));
                }
            }
        }catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.main_nav_list);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    case R.id.go_main:
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.go_community:
                        intent = new Intent(getApplicationContext(), CommunityGoals.class);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        break;

                }
                return false;

            }

        });
        View viewNavHeader = navigationView.getHeaderView(0);
        RoundedImageView profileImage = (RoundedImageView) viewNavHeader.findViewById(R.id.profile_image);
        Picasso.with(getApplicationContext()).load(myUserInfo.url).into(profileImage);
        TextView userName = (TextView) viewNavHeader.findViewById(R.id.username_profile);
        userName.setText(myUserInfo.userName);

        final Menu menu = navigationView.getMenu();
        final SubMenu subMenu = menu.addSubMenu("나의 목표");
        for (int i = 0; i < titleList.size() ; i++) {
            Intent intentNav = new Intent(getApplicationContext(), GoalActivity.class);
            intentNav.putExtra("cid", cidList.get(i));
            subMenu.add(titleList.get(i)).setIcon(R.drawable.ic_done).setIntent(intentNav);
        }

        recyclerViewChatRoom = (RecyclerView) findViewById(R.id.recycler_view_chatroom);

        staggeredGridLayoutManagerChatRoom = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        chatRoomListAdapter = new ChatRoomListAdapter(this, getTestGoalItem());

        addChatButton = (FloatingActionButton) findViewById(R.id.make_chat);
        addChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatCreateDialog = new ChatCreateDialog(ChatRoomActivity.this, pid);
                chatCreateDialog.show();
            }
        });

        recyclerViewChatRoom.setHasFixedSize(true);
        recyclerViewChatRoom.setLayoutManager(staggeredGridLayoutManagerChatRoom);
        recyclerViewChatRoom.setAdapter(chatRoomListAdapter);
    }

    private JSONArray getTestGoalItem() {

        JSONArray test = new JSONArray();

        try {
            JSONObject weight = new JSONObject();
            weight.put("title", "웨이트 트레이닝");
            weight.put("distance", "16.3");
            weight.put("people", "42");
            weight.put("photo", "http://gaslightchiro.com/files/bigstock/2014/07/Beautiful-Young-Female-Exercis-58884710.jpg?w=1060&h=795&a=t");

            JSONObject running = new JSONObject();
            running.put("title", "유산소 운동");
            running.put("distance", "4.3");
            running.put("people", "29");
            running.put("photo", "https://static1.squarespace.com/static/557b65c2e4b00283cf1ca9b0/t/5585d703e4b0d4f1a8bc4dc2/1434834698106/shutterstock_139712587.jpg?format=2500w");

            JSONObject study = new JSONObject();
            study.put("title", "스터디");
            study.put("distance", "4.8");
            study.put("people", "38");
            study.put("photo", "http://media.salemwebnetwork.com/cms/BST/12790-books-bible-study-school-hands.800w.tn.jpg");

            JSONObject explore = new JSONObject();
            explore.put("title", "탐험");
            explore.put("distance", "11.8");
            explore.put("people", "8");
            explore.put("photo", "https://www.gapyear.com/history-of-travel/images/future-travel.jpg");

            JSONObject travel = new JSONObject();
            travel.put("title", "여행");
            travel.put("distance", "14.1");
            travel.put("people", "12");
            travel.put("photo", "http://www.houseoftravel.co.nz/images/default-source/hot-stores/paris84f1d68287e2639a85d9ff0000167a3d.jpg");

            JSONObject programming = new JSONObject();
            programming.put("title", "프로그래밍");
            programming.put("distance", "1.4");
            programming.put("people", "24");
            programming.put("photo", "http://media02.hongkiat.com/programming-myth/programmer.jpg");

            test.put(weight);
            test.put(running);
            test.put(study);
            test.put(explore);
            test.put(travel);
            test.put(programming);
            test.put(programming);
            test.put(programming);
            test.put(programming);
            test.put(programming);
            test.put(programming);
            test.put(programming);
            test.put(programming);
            test.put(programming);
            test.put(programming);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return test;
    }
}
