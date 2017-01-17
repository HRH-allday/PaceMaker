package com.example.q.pacemaker;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.q.pacemaker.Utilities.SendJSON;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by q on 2017-01-16.
 */

public class ChatCreateDialog extends Dialog {

    private String pid;

    private Button mConfirmButton;
    private Button mCancelButton;

    private TextView txt_title, chatRoomTitle;
    private EditText chatName;
    private String themeColor;

    public static Boolean end_flag = false;
    public static String rid;


    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_create_dialog);

        themeColor = getRandomColor();

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        getWindow().setAttributes(lpWindow);

        chatName = (EditText) findViewById(R.id.chat_room_name);
        txt_title = (TextView) findViewById(R.id.txt_title);
        chatRoomTitle = (TextView) findViewById(R.id.chatroom_title);
        mConfirmButton = (Button) findViewById(R.id.btn_right);
        mCancelButton = (Button) findViewById(R.id.btn_left);


        txt_title.setBackgroundColor(Color.parseColor(themeColor));
        chatRoomTitle.setTextColor(Color.parseColor(themeColor));
        mCancelButton.setBackgroundColor(Color.parseColor(themeColor));
        mConfirmButton.setBackgroundColor(Color.parseColor(themeColor));

        if (mCancelClickListener != null && mConfirmClickListener != null) {
            mCancelButton.setOnClickListener(mCancelClickListener);
            mConfirmButton.setOnClickListener(mConfirmClickListener);
        }
    }

    private View.OnClickListener mCancelClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            dismiss();
        }
    };

    private View.OnClickListener mConfirmClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            String name = chatName.getText().toString().trim();
            if(name.equals(""))
                return;
            else{
                try {
                    JSONObject req = new JSONObject();
                    req.put("pid", pid);
                    req.put("color", themeColor);
                    req.put("title", name);
                    req.put("token", FirebaseInstanceId.getInstance().getToken());
                    Log.i("make chatroom!!", req.toString());


                    JSONObject res = new SendJSON(App.server_url + App.routing_new_chat, req.toString(), App.JSONcontentsType).execute().get();
                    if (res != null && res.has("result") && res.getString("result").equals("success")) {
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("room name", name);
                        intent.putExtra("rid", res.getJSONObject("new_chat").getString("_id"));
                        context.startActivity(intent);
                    }
                }catch (JSONException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public ChatCreateDialog(Context ct, String id) {
        super(ct, android.R.style.Theme_Translucent_NoTitleBar);
        context = ct;
        pid = id;

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
