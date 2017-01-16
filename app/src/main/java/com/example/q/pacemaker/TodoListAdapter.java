package com.example.q.pacemaker;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by q on 2017-01-14.
 */

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder>{
    private ArrayList<TodoListData> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView)view.findViewById(R.id.todo_color);
            mTextView = (TextView)view.findViewById(R.id.todo_text);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TodoListAdapter(ArrayList<TodoListData> myDataset) {
        mDataset = myDataset;
    }

    public void addItem(TodoListData tld, int position){
        Log.i("entered", "pos : "+position);
        mDataset.add(position, tld);
        notifyItemInserted(position);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).text);
        Log.i("reached",mDataset.get(position).text );
        holder.mImageView.setBackgroundColor(Color.parseColor(mDataset.get(position).color));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
