package com.example.q.pacemaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mainTodoList;
    private RecyclerView.Adapter mainAdapter;
    private RecyclerView.LayoutManager mainLayoutManager;
    private ArrayList<ArrayList<TodoListData>> todoList;
    private ArrayList<String> titleList;

    private String[] mPlanetTitles = {"1번","2번"};
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    public static UserInfo myInfo = new UserInfo("홍영규", "testurl", "1234");


/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.routine_fragment, null);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());


        backgroundList = (RecyclerView) view.findViewById(R.id.routine_recyclerview);
        backgroundList.setLayoutManager(mLayoutManager);

        adapter = new TodoListAdapter(todoListDatas);
        Log.i("really", todoListDatas.get(1).text);

        backgroundList.setAdapter(adapter);

        return view;
    }
    */

    /*
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<CustomizeData> customizeDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<TodoListData> tld = new ArrayList<>();
        ArrayList<ArrayList<TodoListData>> routine = new ArrayList<>();
        tld.add(new TodoListData("밥 먹기", "#ff1616"));
        tld.add(new TodoListData("개발하기", "#ff1616"));
        for(int i = 0; i < 7; i++){
            routine.add(tld);
        }
        CustomizeData cd = new CustomizeData(0, tld);
        CustomizeData cd2 = new CustomizeData(1, routine, 0);
        customizeDatas.add(cd);
        customizeDatas.add(cd2);

        mAdapter = new GoalCardViewAdapter(getSupportFragmentManager(), customizeDatas);
        mRecyclerView.setAdapter(mAdapter);

    }
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO : todoList, titleList 에 할일 목록 넣기
        //data setting
        ArrayList<TodoListData> tld = new ArrayList<>();
        ArrayList<ArrayList<TodoListData>> routine = new ArrayList<>();
        titleList = new ArrayList<>();
        todoList = new ArrayList<>();
        tld.add(new TodoListData("밥 먹기", "#ff1616"));
        tld.add(new TodoListData("개발하기", "#ff1616"));
        for(int i = 0; i < 7; i++){
            todoList.add(tld);
            titleList.add(i+"번째 할 일 " );
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
