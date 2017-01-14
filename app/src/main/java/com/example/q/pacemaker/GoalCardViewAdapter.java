package com.example.q.pacemaker;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by q on 2017-01-14.
 */

public class GoalCardViewAdapter extends RecyclerView.Adapter<GoalCardViewAdapter.ViewHolder> {
    private ArrayList<CustomizeData> customizeDatas;
    private ArrayList<TodoListData> mTodoList;




    private ArrayList<ArrayList<TodoListData>> mRoutineList;
    public RecyclerView.Adapter mAdapter;
    private int[] mDataSetTypes = {0,1};
    public AppCompatActivity activity;

    public static final int TODO = 0;
    public static final int ROUTINE = 1;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View v){
            super(v);
        }
    }
    public class TodoViewHolder extends ViewHolder{
        // each data item is just a string in this case
        public TextView mTextView;
        public RecyclerView mRecyclerView;
        public RecyclerView.LayoutManager mLayoutManager;

        public TodoViewHolder(View view) {
            super(view);
            mTextView = (TextView)view.findViewById(R.id.todo);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.todoRecyclerView);
            mLayoutManager = new LinearLayoutManager(view.getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

        }
    }

    public class RoutineViewHolder extends ViewHolder implements TabLayout.OnTabSelectedListener{
        // each data item is just a string in this case
        public TabLayout tabLayout;
        public RoutineAdapter adapter;
        public ViewPager viewPager;

        public RoutineViewHolder(View view) {
            super(view);
            tabLayout = (TabLayout) view.findViewById(R.id.routine_tablayout);

            tabLayout.addTab(tabLayout.newTab().setText("월"));
            tabLayout.addTab(tabLayout.newTab().setText("화"));
            tabLayout.addTab(tabLayout.newTab().setText("수"));
            tabLayout.addTab(tabLayout.newTab().setText("목"));
            tabLayout.addTab(tabLayout.newTab().setText("금"));
            tabLayout.addTab(tabLayout.newTab().setText("토"));
            tabLayout.addTab(tabLayout.newTab().setText("일"));

            viewPager = (ViewPager) view.findViewById(R.id.routine_viewpager);
            adapter = new RoutineAdapter(activity.getSupportFragmentManager(), mRoutineList.get(0),mRoutineList.get(1),mRoutineList.get(2),mRoutineList.get(3),mRoutineList.get(4),mRoutineList.get(5),mRoutineList.get(6));
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

    public GoalCardViewAdapter (AppCompatActivity act, ArrayList<CustomizeData> customizeDatas){
        this.activity = act;
        this.customizeDatas = customizeDatas;
        this.mTodoList = customizeDatas.get(0).todoListDatas;
        this.mRoutineList = customizeDatas.get(1).routineDatas;
        this.mAdapter = new TodoListAdapter(mTodoList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View v;
        if(viewType == TODO){
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todo_card, viewGroup, false);
            return new TodoViewHolder(v);
        }else if(viewType == ROUTINE){
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.routine_card, viewGroup, false);
            return new RoutineViewHolder(v);
        }else{
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todo_card, viewGroup, false);
            return new TodoViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(holder.getItemViewType() == TODO){
            TodoViewHolder vh = (TodoViewHolder) holder;
            vh.mTextView.setText("오늘의 할 일");
            vh.mRecyclerView.setAdapter(mAdapter);
        }else if(holder.getItemViewType() == ROUTINE){
            RoutineViewHolder vh = (RoutineViewHolder) holder;
        }else{
            TodoViewHolder vh = (TodoViewHolder) holder;
            vh.mTextView.setText("오늘의 할 일");
            vh.mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return customizeDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSetTypes[position];
    }


}
