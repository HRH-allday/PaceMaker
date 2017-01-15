package com.example.q.pacemaker;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private ArrayList<ArrayList<TodoListData>> mRoutineList;
    public RecyclerView.Adapter mAdapter;

    public TextView mTextView;
    public RecyclerView mRecyclerView;
    public RecyclerView.LayoutManager mLayoutManager;

    public TabLayout tabLayout;
    public RoutineAdapter adapter;
    public ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
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
        mAdapter = new TodoListAdapter(mTodoList);

        todoView = (CardView) findViewById(R.id.cardview);
        routineView = (CardView) findViewById(R.id.cardview2);
        mTextView = (TextView) findViewById(R.id.todo);
        mRecyclerView = (RecyclerView)  findViewById(R.id.todoRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mTextView.setText("오늘의 할 일");
        mRecyclerView.setAdapter(mAdapter);

        tabLayout = (TabLayout) findViewById(R.id.routine_tablayout);

        tabLayout.addTab(tabLayout.newTab().setText("월"));
        tabLayout.addTab(tabLayout.newTab().setText("화"));
        tabLayout.addTab(tabLayout.newTab().setText("수"));
        tabLayout.addTab(tabLayout.newTab().setText("목"));
        tabLayout.addTab(tabLayout.newTab().setText("금"));
        tabLayout.addTab(tabLayout.newTab().setText("토"));
        tabLayout.addTab(tabLayout.newTab().setText("일"));

        viewPager = (ViewPager) findViewById(R.id.routine_viewpager);
        adapter = new RoutineAdapter(getSupportFragmentManager(), mRoutineList.get(0),mRoutineList.get(1),mRoutineList.get(2),mRoutineList.get(3),mRoutineList.get(4),mRoutineList.get(5),mRoutineList.get(6));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(this);



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
