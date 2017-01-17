package com.example.q.pacemaker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by q on 2017-01-15.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{
    private ArrayList<ChatMessage> mDataset;
    private Context context;

    public static final int MINE = 0;
    public static final int OTHERS = 1;
    public static final int ENTERED = 2;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class MyMessageViewHolder extends ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mImageView;
        public TextView myMessage;

        public MyMessageViewHolder(View view) {
            super(view);
            myMessage = (TextView)view.findViewById(R.id.myMessage);
            mImageView = (ImageView)view.findViewById(R.id.user_image);
            mTextView = (TextView)view.findViewById(R.id.user_name);
        }
    }

    public class OtherMessageViewHolder extends ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mImageView;
        public TextView otherMessage;

        public OtherMessageViewHolder(View view) {
            super(view);
            otherMessage = (TextView)view.findViewById(R.id.otherMessage);
            mImageView = (ImageView)view.findViewById(R.id.user_image);
            mTextView = (TextView)view.findViewById(R.id.user_name);
        }
    }

    public class UserEnteredViewHolder extends ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;

        public UserEnteredViewHolder(View view) {
            super(view);
            mTextView = (TextView)view.findViewById(R.id.user_entered);
        }
    }

    public class UserExitedViewHolder extends ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;

        public UserExitedViewHolder(View view) {
            super(view);
            mTextView = (TextView)view.findViewById(R.id.user_exited);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatAdapter(ArrayList<ChatMessage> myDataset, Context ct) {
        mDataset = myDataset;
        context = ct;
    }

    public void addItem(ChatMessage tld){
        mDataset.add(tld);
        notifyItemInserted(mDataset.size());
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // create a new view
        View v;
        if (viewType == MINE) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.chat_item_me, viewGroup, false);

            return new MyMessageViewHolder(v);
        }else if (viewType == OTHERS){
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.chat_item_other, viewGroup, false);
            return new OtherMessageViewHolder(v);
        }else if (viewType == ENTERED){
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.chat_item_user_entered, viewGroup, false);
            return new UserEnteredViewHolder(v);
        }else {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.chat_item_user_exited, viewGroup, false);
            return new UserEnteredViewHolder(v);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (viewHolder.getItemViewType() == MINE) {
            MyMessageViewHolder holder = (MyMessageViewHolder) viewHolder;
            holder.mTextView.setText(mDataset.get(position).user.userName);
            holder.myMessage.setText(mDataset.get(position).message);
            Picasso.with(context).load(mDataset.get(position).user.url).into(holder.mImageView);
        }
        else if (viewHolder.getItemViewType() == OTHERS){
            OtherMessageViewHolder holder = (OtherMessageViewHolder) viewHolder;
            holder.mTextView.setText(mDataset.get(position).user.userName);
            holder.otherMessage.setText(mDataset.get(position).message);
            Picasso.with(context).load(mDataset.get(position).user.url).into(holder.mImageView);
        }
        else if (viewHolder.getItemViewType() == ENTERED){
            UserEnteredViewHolder holder = (UserEnteredViewHolder) viewHolder;
            holder.mTextView.setText("--- " + mDataset.get(position).user.userName + "님이 입장하셨습니다 ---");
        }
        else {
            UserExitedViewHolder holder = (UserExitedViewHolder) viewHolder;
            holder.mTextView.setText("--- " + mDataset.get(position).user.userName + "님이 퇴장하셨습니다 ---");
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataset.get(position).type;
    }
}