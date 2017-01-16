package com.example.q.pacemaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by q on 2017-01-14.
 */

public class GoalActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    private ArrayList<CustomizeData> customizeDatas = new ArrayList<>();
    private CardView todoView;
    private CardView routineView;

    private ArrayList<TodoListData> mTodoList;
    private ArrayList<TodoListData> mMondayList;
    private ArrayList<TodoListData> mTuesdayList;
    private ArrayList<TodoListData> mWednesdayList;
    private ArrayList<TodoListData> mThursdayList;
    private ArrayList<TodoListData> mFridayList;
    private ArrayList<TodoListData> mSatdayList;
    private ArrayList<TodoListData> mSundayList;
    private ArrayList<ArrayList<TodoListData>> mRoutineList;

    public String title;
    public String description;
    private String cloneID;



    public TextView todoTextView;
    public RecyclerView todoRecyclerView;
    public RecyclerView.LayoutManager mLayoutManager;
    public RecyclerView.Adapter todoAdapter;

    public TabLayout tabLayout;
    public RoutineAdapter adapter;
    public ViewPager viewPager;

    private ArrayList<TodoListData> mMemoList;
    public Button memo_plus;
    public EditText memoEditView;
    public RecyclerView memoRecyclerView;
    public RecyclerView.Adapter memoAdapter;
    public RecyclerView.LayoutManager memoLayoutManager;

    private Button shareButton;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        cloneID = intent.getStringExtra("cloneID");


        // TODO: title, description, todo list, routine, memo 받아서 넣기

        // data setting
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
        mTodoList = tld;
        mRoutineList = routine;

        // nav drawer setting
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





        // todo card
        todoView = (CardView) findViewById(R.id.cardview);

        todoTextView = (TextView) findViewById(R.id.todo);
        todoRecyclerView = (RecyclerView)  findViewById(R.id.todoRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        todoRecyclerView.setLayoutManager(mLayoutManager);
        todoTextView.setText("오늘의 할 일");
        todoAdapter = new TodoListAdapter(mTodoList);
        todoRecyclerView.setAdapter(todoAdapter);

        // routine card

        routineView = (CardView) findViewById(R.id.routine_cardview);
        tabLayout = (TabLayout) findViewById(R.id.routine_tablayout);

        tabLayout.addTab(tabLayout.newTab().setText("월"));
        tabLayout.addTab(tabLayout.newTab().setText("화"));
        tabLayout.addTab(tabLayout.newTab().setText("수"));
        tabLayout.addTab(tabLayout.newTab().setText("목"));
        tabLayout.addTab(tabLayout.newTab().setText("금"));
        tabLayout.addTab(tabLayout.newTab().setText("토"));
        tabLayout.addTab(tabLayout.newTab().setText("일"));

        viewPager = (ViewPager) findViewById(R.id.routine_viewpager);
        adapter = new RoutineAdapter(getSupportFragmentManager(), mRoutineList.get(0),mRoutineList.get(1),mRoutineList.get(2),mRoutineList.get(3),mRoutineList.get(4),mRoutineList.get(5),mRoutineList.get(6), 1);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(this);

        // memo card
        todoView = (CardView) findViewById(R.id.cardview);
        memo_plus = (Button) findViewById(R.id.memo_button);
        memoEditView = (EditText) findViewById(R.id.memo_edit);
        memoRecyclerView = (RecyclerView)  findViewById(R.id.memoRecyclerView);
        memoLayoutManager = new LinearLayoutManager(this);
        memoRecyclerView.setLayoutManager(memoLayoutManager);
        memoAdapter = new TodoListAdapter(new ArrayList<TodoListData>());
        memoRecyclerView.setAdapter(memoAdapter);

        String memo;
        memo_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edit = memoEditView.getText().toString().trim();
                if(edit.equals(""))
                    return;
                else
                    ((TodoListAdapter) memoAdapter).addItem(new TodoListData(edit, "#ff1616"), 0);

            }
        });

        //share
        shareButton = (Button) findViewById(R.id.share_button);
        title = "제목";
        description = "설명";

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msg = new Intent(Intent.ACTION_SEND);
                msg.addCategory(Intent.CATEGORY_DEFAULT);
                msg.putExtra(Intent.EXTRA_TEXT, description);
                msg.putExtra(Intent.EXTRA_SUBJECT, title);
                msg.setType("text/plain");
                startActivity(Intent.createChooser(msg, "공유"));

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.invisible_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_button){
            Intent intent = new Intent(getApplicationContext(), CommunityActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
