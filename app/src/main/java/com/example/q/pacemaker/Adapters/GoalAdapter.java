package com.example.q.pacemaker.Adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.q.pacemaker.R;
import com.squareup.picasso.Picasso;

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
            Picasso.with(activity.getApplicationContext()).load(url).into(holder.imageView);
            holder.textView.setText(goalObj.getString("title"));

            // OnClickListener
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(activity, "Clicked!", Toast.LENGTH_SHORT).show();
                    /*
                    Intent intent = new Intent(activity, <activity_name>.class);
                    intent.putExtra("<field_name>", goalObj.toString());
                    activity.startActivity(intent);
                    */
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
        private ImageView imageView;
        private TextView textView;

        protected ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view_community_goal);
            imageView = (ImageView) itemView.findViewById(R.id.community_goal_photo);
            textView = (TextView) itemView.findViewById(R.id.community_goal_name);
        }
    }
}

