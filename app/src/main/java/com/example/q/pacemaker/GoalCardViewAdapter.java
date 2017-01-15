package com.example.q.pacemaker;

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
    private ArrayList<ArrayList<TodoListData>> mTodoList;
    private ArrayList<String> mTitleList;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public RecyclerView mRecyclerView;
        public RecyclerView.LayoutManager mLayoutManager;
        public RecyclerView.Adapter mAdapter;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView)view.findViewById(R.id.todo);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.todoRecyclerView);
            mLayoutManager = new LinearLayoutManager(view.getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

        }
    }

    public GoalCardViewAdapter (ArrayList<ArrayList<TodoListData>> todoListDatas, ArrayList<String> titleList){
        this.mTodoList = todoListDatas;
        this.mTitleList = titleList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View v;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todo_card, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewHolder vh = holder;
        vh.mTextView.setText(mTitleList.get(position));
        vh.mAdapter = new TodoListAdapter(mTodoList.get(position));
        vh.mRecyclerView.setAdapter(vh.mAdapter);
    }

    @Override
    public int getItemCount() {
        return mTodoList.size();
    }



}
