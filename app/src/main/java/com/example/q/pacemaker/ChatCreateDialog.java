package com.example.q.pacemaker;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Thread.sleep;

/**
 * Created by q on 2017-01-16.
 */

public class ChatCreateDialog extends Dialog {

    private String pid;

    private Button mConfirmButton;
    private Button mCancelButton;

    private EditText chatName;
    private RadioButton radio_public;
    private RadioButton radio_private;

    public static Boolean end_flag = false;
    public static String rid;


    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_create_dialog);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        getWindow().setAttributes(lpWindow);

        chatName = (EditText) findViewById(R.id.chat_room_name);
        radio_public = (RadioButton) findViewById(R.id.chat_public);


        mConfirmButton = (Button) findViewById(R.id.btn_right);
        mCancelButton = (Button) findViewById(R.id.btn_left);

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
            Boolean pb = radio_public.isChecked();
            if(name.equals(""))
                return;
            else{
                try {
                    JSONObject jobj = new JSONObject();
                    jobj.put("name", name);
                    jobj.put("public", pb);
                    jobj.put("creator", MainActivity.myInfo.userName);
                    jobj.put("token", MainActivity.myInfo.token);
                    jobj.put("pid", pid);
                    PostThread p = new PostThread(jobj, "/new_chat");
                    p.start();
                    while(!end_flag){
                        sleep(200);
                    }
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("room name", name);
                    intent.putExtra("rid", rid);
                    context.startActivity(intent);
                    dismiss();
                }catch (JSONException e){
                    e.printStackTrace();
                }catch(InterruptedException e){
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
}
