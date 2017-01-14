package com.example.q.pacemaker.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.q.pacemaker.GoalActivity;
import com.example.q.pacemaker.R;
import com.squareup.picasso.Picasso;
import com.example.q.pacemaker.App;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.ViewHolder> {

    private final Activity activity;
    private JSONArray goalArray;

    public GoalAdapter(Activity activity, JSONArray goalArray) {
        assert activity != null;
        this.activity = activity;
        this.goalArray = goalArray;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_goal_item, parent, false);
        return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.position = holder.getAdapterPosition();
        final JSONObject goalObj = goalArray.optJSONObject(position);

        try {
            String url = goalObj.getString("photo");
            // Bitmap bmp = new LoadImage().execute(url).get();
            // holder.imageView.setImageBitmap(bmp);
            Picasso.with(activity.getApplicationContext()).load(url).into(holder.goalPhoto);
            holder.goalTitle.setText(goalObj.getString("title"));
            holder.goalPeople.setText(goalObj.getString("people") + "명 참여중");

            // OnClickListener
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, GoalActivity.class);
                    // intent.putExtra("<field_name>", goalObj.toString());
                    activity.startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (goalArray == null)
            return -1;
        return goalArray.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private int position;
        private CardView cardView;
        private ImageView goalPhoto;
        private TextView goalTitle;
        private TextView goalPeople;

        protected ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view_community_goal);
            goalPhoto = (ImageView) itemView.findViewById(R.id.community_goal_photo);
            goalTitle = (TextView) itemView.findViewById(R.id.community_goal_name);
            goalPeople = (TextView) itemView.findViewById(R.id.community_goal_people);

            goalTitle.setTypeface(App.NanumBarunGothicLight);
            goalPeople.setTypeface(App.NanumBarunGothicLight);
        }
    }
}

