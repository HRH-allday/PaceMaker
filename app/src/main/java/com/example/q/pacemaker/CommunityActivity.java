package com.example.q.pacemaker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

/**
 * Created by q on 2017-01-15.
 */

public class CommunityActivity extends Activity {
    private ArrayList<UserInfo> userList;
    private RecyclerView userRecyclerView;
    private RecyclerView.Adapter userAdapter;
    private RecyclerView.LayoutManager userLayoutManager;
    private FloatingActionButton enterChatButton;

    private String token;
    private String photoUrl;
    private String pid;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        token = FirebaseInstanceId.getInstance().getToken();

        Intent intentFrom = getIntent();
        pid = intentFrom.getStringExtra("pid");

        userList = new ArrayList<>();

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

        //TODO: 동참인원 정보 받아와서 userList에 채워넣기 username, 사진 url 필요
        try {
            JSONObject req = new JSONObject();
            req.put("pid", pid);

            JSONObject res = new SendJSON(App.server_url + App.routing_goal_info, req.toString(), App.JSONcontentsType).execute().get();
            if (res != null && res.has("result") && res.getString("result").equals("success")) {
                JSONObject goal = res.getJSONObject("goal");
                JSONArray users = res.getJSONArray("participants");
                for(int i = 0; i < users.length() ; i++){
                    JSONObject partJobj = users.getJSONObject(i);
                    userList.add(new UserInfo(partJobj.getString("name"), partJobj.getString("photo"), partJobj.getString("token")));
                    Log.i("participant name", partJobj.getString("name"));
                }
                photoUrl = goal.getString("photo");
                title = goal.getString("title");

            }
        }catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
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

        ImageView commBackground = (ImageView) findViewById(R.id.community_image);
        Picasso.with(getApplicationContext()).load(photoUrl).into(commBackground);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.community_collapsing_toolbar);
        collapsingToolbarLayout.setTitle(title);

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

    }
}
