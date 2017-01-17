package com.example.q.pacemaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;

import com.example.q.pacemaker.Utilities.RoundedImageView;
import com.example.q.pacemaker.Utilities.SendJSON;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mainTodoList;
    private RecyclerView.Adapter mainAdapter;
    private RecyclerView.LayoutManager mainLayoutManager;
    private ArrayList<ArrayList<TodoListData>> todoList;
    public static ArrayList<String> titleList;
    public static ArrayList<String> cidList;

    public String token;

    public static UserInfo myInfo = new UserInfo("홍영규", "testurl", "1234");

    public static UserInfo myUserInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        token = FirebaseInstanceId.getInstance().getToken();


        //TODO : todoList, titleList 에 할일 목록 넣기
        //data setting
        titleList = new ArrayList<>();
        todoList = new ArrayList<>();
        cidList = new ArrayList<>();
        try {
            JSONObject req = new JSONObject();
            req.put("token", token);

            JSONObject res = new SendJSON(App.server_url + App.routing_main, req.toString(), App.JSONcontentsType).execute().get();

            if (res != null && res.has("result") && res.getString("result").equals("success")) {
                JSONArray goalJarr = res.getJSONArray("goals");
                JSONArray titleJarr = res.getJSONArray("titles");
                JSONObject userData = res.getJSONObject("user");
                Log.i("sent info", res.toString());
                Log.i("length", ": "+goalJarr.length());
                for(int i = 0 ; i < goalJarr.length() ; i++){
                    titleList.add(titleJarr.getString(i));
                    JSONObject goal = goalJarr.getJSONObject(i);
                    cidList.add(goal.getString("_id"));
                    ArrayList<TodoListData> tld = new ArrayList<>();
                    JSONArray todos = goal.getJSONArray("todo");
                    for(int j = 0 ; j < todos.length() ; j++){
                        tld.add(new TodoListData(todos.getString(j), "#ff1616"));
                    }
                    JSONArray todayRoutines;
                    Calendar cal= Calendar.getInstance();
                    switch (cal.get(Calendar.DAY_OF_WEEK)){
                        case 1:
                            todayRoutines = goal.getJSONArray("sun");
                            break;
                        case 2:
                            todayRoutines = goal.getJSONArray("mon");
                            break;
                        case 3:
                            todayRoutines = goal.getJSONArray("tue");
                            break;
                        case 4:
                            todayRoutines = goal.getJSONArray("wed");
                            break;
                        case 5:
                            todayRoutines = goal.getJSONArray("thu");
                            break;
                        case 6:
                            todayRoutines = goal.getJSONArray("fri");
                            break;
                        default:
                            todayRoutines = res.getJSONArray("sat");
                            break;
                    }
                    for(int j = 0 ; j < todayRoutines.length() ; j++){
                        tld.add(new TodoListData(todayRoutines.getString(j), "#ff1616"));
                    }
                    todoList.add(tld);
                }

                myUserInfo = new UserInfo(userData.getString("name"), userData.getString("photo"), userData.getString("token"));

            }
        } catch (JSONException | InterruptedException | ExecutionException e) {
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
            Intent intent = new Intent(getApplicationContext(), GoalActivity.class);
            intent.putExtra("cid", cidList.get(i));
            subMenu.add(titleList.get(i)).setIcon(R.drawable.ic_done).setIntent(intent);
        }




        mainTodoList = (RecyclerView) findViewById(R.id.main_recycler_view);
        mainLayoutManager = new LinearLayoutManager(this);
        mainTodoList.setLayoutManager(mainLayoutManager);
        mainAdapter = new GoalCardViewAdapter(todoList, titleList);
        mainTodoList.setAdapter(mainAdapter);


        FloatingActionButton add_goal = (FloatingActionButton) findViewById(R.id.main_add_goal);

        add_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GoalActivity.class);
                startActivity(intent);
            }
        });
        FloatingActionButton community = (FloatingActionButton) findViewById(R.id.main_community);

        community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommunityGoals.class);
                startActivity(intent);
            }
        });
        FloatingActionButton register = (FloatingActionButton) findViewById(R.id.main_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GoalRegisterActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton chatroom = (FloatingActionButton) findViewById(R.id.main_chatroom);
        chatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatRoomActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton googlemap = (FloatingActionButton) findViewById(R.id.main_googlemap);
        googlemap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

    }
}
