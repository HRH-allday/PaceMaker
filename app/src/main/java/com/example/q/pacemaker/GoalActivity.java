package com.example.q.pacemaker;

import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by q on 2017-01-14.
 */

public class GoalActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<CustomizeData> customizeDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
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

        mAdapter = new GoalCardViewAdapter(this, customizeDatas);
        mRecyclerView.setAdapter(mAdapter);

    }
}
