package com.example.q.pacemaker;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by q on 2017-01-14.
 */

public class GoalActivity extends Activity {
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
        tld.add(new TodoListData("밥 먹기", "#ff1616"));
        CustomizeData cd = new CustomizeData(0, tld);
        customizeDatas.add(cd);

        mAdapter = new GoalCardViewAdapter(customizeDatas);
        mRecyclerView.setAdapter(mAdapter);

    }
}
