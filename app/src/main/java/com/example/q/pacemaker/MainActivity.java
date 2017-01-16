package com.example.q.pacemaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.q.pacemaker.Utilities.SendJSON;
import com.google.firebase.iid.FirebaseInstanceId;

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
    private ArrayList<String> titleList;

    private String[] mPlanetTitles = {"1번","2번"};
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    public String token;

    public static UserInfo myInfo = new UserInfo("홍영규", "testurl", "1234");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        token = FirebaseInstanceId.getInstance().getToken();


        //TODO : todoList, titleList 에 할일 목록 넣기
        //data setting
        titleList = new ArrayList<>();
        todoList = new ArrayList<>();
        try {
            JSONObject req = new JSONObject();
            req.put("token", token);

            JSONObject res = new SendJSON(App.server_url + App.routing_main, req.toString(), App.JSONcontentsType).execute().get();

            if (res != null && res.has("result") && res.getString("result").equals("success")) {
                JSONArray goalJarr = res.getJSONArray("goals");
                JSONArray titleJarr = res.getJSONArray("titles");
                Log.i("length", ": "+goalJarr.length());
                for(int i = 0 ; i < goalJarr.length() ; i++){
                    titleList.add(titleJarr.getString(i));
                    JSONObject goal = goalJarr.getJSONObject(i);
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
