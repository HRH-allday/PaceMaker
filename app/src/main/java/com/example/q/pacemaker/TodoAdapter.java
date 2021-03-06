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

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private ArrayList<CustomizeData> mDataset;
    private static ArrayList<TodoListData> mTodoList = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public RecyclerView mRecyclerView;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView)view.findViewById(R.id.todo);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.todoRecyclerView);
            mLayoutManager = new LinearLayoutManager(view.getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new TodoListAdapter(mTodoList);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TodoAdapter(ArrayList<CustomizeData> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_view, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText("오늘의 할 일");
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}