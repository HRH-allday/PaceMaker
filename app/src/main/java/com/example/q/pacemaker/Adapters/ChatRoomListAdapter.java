package com.example.q.pacemaker.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.q.pacemaker.App;
import com.example.q.pacemaker.ChatActivity;
import com.example.q.pacemaker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatRoomListAdapter extends RecyclerView.Adapter<ChatRoomListAdapter.ViewHolder> {

    private final Activity activity;
    private JSONArray chatRoomListArray;


    public ChatRoomListAdapter(Activity activity, JSONArray chatRoomListArray) {
        assert activity != null;
        this.activity = activity;
        this.chatRoomListArray = chatRoomListArray;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_room_item, parent, false);
        return new ViewHolder(layoutView);
    }

    public void dataSwap(JSONArray jarr){
        chatRoomListArray = jarr;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.position = holder.getAdapterPosition();
        final JSONObject chatRoomObj = chatRoomListArray.optJSONObject(position);

        try {
            // Bitmap bmp = new LoadImage().execute(url).get();
            // holder.imageView.setImageBitmap(bmp);
            // Picasso.with(activity.getApplicationContext()).load(url).into(holder.chatRoomPhoto);
            holder.chatRoomPhoto.setBackgroundColor(Color.parseColor(chatRoomObj.getString("color")));
            holder.chatRoomTitle.setText(chatRoomObj.getString("title"));
            holder.chatRoomPeople.setText(chatRoomObj.getString("people") + "명 참여중");

            // OnClickListener
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(activity, ChatActivity.class);
                        intent.putExtra("room name", chatRoomObj.getString("title"));
                        intent.putExtra("rid", chatRoomObj.getString("_id"));
                        activity.startActivity(intent);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (chatRoomListArray == null)
            return -1;
        return chatRoomListArray.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private int position;
        private CardView cardView;
        private RelativeLayout relativeLayout;
        private ImageView chatRoomPhoto;
        private TextView chatRoomTitle;
        private TextView chatRoomPeople;

        protected ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view_chatroom);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.chatRoomLayout);
            chatRoomPhoto = (ImageView) itemView.findViewById(R.id.chatroom_photo);
            chatRoomTitle = (TextView) itemView.findViewById(R.id.chatroom_name);
            chatRoomPeople = (TextView) itemView.findViewById(R.id.chatroom_people);

            /*
            DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;

            Log.e("WIDTH", String.valueOf(width));
            Log.e("HEIGHT", String.valueOf(height));

            //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
            //relativeLayout.setLayoutParams(params);
            */

            chatRoomTitle.setTypeface(App.NanumBarunGothicLight);
            chatRoomPeople.setTypeface(App.NanumBarunGothicLight);
        }
    }

    public String getRandomColor() {
        String[] colors = {
                "#329ebb",
                "#b1bf52",
                "#ebdf41",
                "#d53492",
                "#e48c5c"
        };

        double random = Math.random();
        int index = (int) (random * colors.length);
        return colors[index];
    }
}

