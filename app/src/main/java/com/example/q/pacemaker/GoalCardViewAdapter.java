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

public class GoalCardViewAdapter extends RecyclerView.Adapter<GoalCardViewAdapter.ViewHolder>{
    private ArrayList<CustomizeData> customizeDatas;
    private ArrayList<TodoListData> mTodoList;
    private int[] mDataSetTypes = {0};

    public static final int TODO = 0;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View v){
            super(v);
        }
    }
    public class TodoViewHolder extends ViewHolder{
        // each data item is just a string in this case
        public TextView mTextView;
        public RecyclerView mRecyclerView;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;

        public TodoViewHolder(View view) {
            super(view);
            mTextView = (TextView)view.findViewById(R.id.todo);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.todoRecyclerView);
            mLayoutManager = new LinearLayoutManager(view.getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new TodoListAdapter(mTodoList);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    public GoalCardViewAdapter (ArrayList<CustomizeData> customizeDatas){
        this.customizeDatas = customizeDatas;
        this.mTodoList = customizeDatas.get(0).todoListDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View v;
        if(viewType == TODO){
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todo_card, viewGroup, false);
            return new TodoViewHolder(v);
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
        }else{
            TodoViewHolder vh = (TodoViewHolder) holder;
            vh.mTextView.setText("오늘의 할 일");
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
