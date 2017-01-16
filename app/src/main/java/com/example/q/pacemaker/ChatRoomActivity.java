package com.example.q.pacemaker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by q on 2017-01-15.
 */

public class ChatRoomActivity extends Activity {

    private FloatingActionButton enterChatButton;
    private ChatCreateDialog chatCreateDialog;
    private String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        // TODO : setup pid
        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");

        ImageButton imageButton = (ImageButton) findViewById(R.id.enter_chat_temp);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(intent);
            }
        });

        enterChatButton = (FloatingActionButton) findViewById(R.id.make_chat);
        enterChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatCreateDialog = new ChatCreateDialog(ChatRoomActivity.this, pid);
                chatCreateDialog.show();
            }
        });

    }
}
