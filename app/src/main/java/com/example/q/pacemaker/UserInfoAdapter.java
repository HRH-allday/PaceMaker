package com.example.q.pacemaker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by q on 2017-01-15.
 */

public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.ViewHolder>{
    private ArrayList<UserInfo> mDataset;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView)view.findViewById(R.id.user_image);
            mTextView = (TextView)view.findViewById(R.id.user_name);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public UserInfoAdapter(ArrayList<UserInfo> myDataset, Context ct) {
        mDataset = myDataset;
        context = ct;
    }

    public void addItem(UserInfo tld, int position){
        mDataset.add(position, tld);
        notifyItemInserted(position);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public UserInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).userName);
        //Picasso.with(context).load(mDataset.get(position).url).into(holder.mImageView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
