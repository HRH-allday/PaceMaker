package com.example.q.pacemaker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.q.pacemaker.Utilities.SendJSON;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by q on 2017-01-15.
 */



public class ChatActivity extends Activity {
    private Socket mSocket;
    private String ec2url = "http://ec2-52-78-200-87.ap-northeast-2.compute.amazonaws.com:3000";


    private EditText chatEdit;
    private Button chatSendButton;
    private RecyclerView chatRecyclerView;
    private RecyclerView.Adapter chatAdapter;
    private RecyclerView.LayoutManager chatLayoutManager;

    private String roomName;
    private String roomID;

    private String token;

    private ArrayList<UserInfo> participant = new ArrayList<>();

    private UserInfo profile;
    {
        try {
            mSocket = IO.socket(ec2url);
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        token = FirebaseInstanceId.getInstance().getToken();

        try {
            JSONObject req = new JSONObject();
            req.put("token", token);

            JSONObject res = new SendJSON(App.server_url + App.routing_user_info, req.toString(), App.JSONcontentsType).execute().get();
            if (res != null && res.has("result") && res.getString("result").equals("success")) {
                JSONObject userData = res.getJSONObject("user");
                Log.i("userData", userData.toString());
                profile = new UserInfo(userData.getString("name"), userData.getString("photo"), userData.getString("token"));
            }
        }catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //TODO: roomName, roomID intent로 넘겨 받기
        Intent intent = getIntent();
        roomName = intent.getStringExtra("room name");
        roomID = intent.getStringExtra("rid");

        chatEdit = (EditText) findViewById(R.id.chat_edit);
        chatSendButton = (Button) findViewById(R.id.chat_button);

        chatRecyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);
        chatLayoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(chatLayoutManager);
        chatAdapter = new ChatAdapter(new ArrayList<ChatMessage>(), getApplicationContext());
        chatRecyclerView.setAdapter(chatAdapter);

        mSocket.connect();
        mSocket.on("new message", onNewMessage);
        mSocket.on("user joined", onUserJoined);
        mSocket.on("member info", onMemberInfo);
        mSocket.on("exit info", onExitInfo);

        mSocket.emit("user joined", profile.userName, roomName, roomID);
        mSocket.emit("member info", roomID);

        //TODO: db에서 participant 정보 받아오기

       chatSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chatMessage = chatEdit.getText().toString().trim();
                if(chatMessage.equals(""))
                    return;
                else{
                    chatEdit.setText("");
                    addMessage(profile, chatMessage, 0);
                    mSocket.emit("new message", token, chatMessage);
                }
            }
        });
    }

    public void addMessage(UserInfo userInfo, String msg, int type){
        ((ChatAdapter) chatAdapter).addItem(new ChatMessage(msg, userInfo, type));
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    String url;
                    String token;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                        url = data.getString("photo");
                        token = data.getString("token");
                    } catch (JSONException e) {
                        return;
                    }

                    // add the message to view
                    Log.i("test",message);
                    addMessage(new UserInfo(username, url, token), message, 1);
                }
            });
        }
    };

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        return;
                    }
                    addMessage(new UserInfo(username, "", ""), "", 2);

                }
            });
        }
    };

    private Emitter.Listener onMemberInfo = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    JSONArray participants;
                    try {
                        participants =  data.getJSONArray("participants");
                        participant = new ArrayList<>();
                        for(int i = 0; i< participants.length() ; i++){
                            JSONObject part = participants.getJSONObject(i);
                            participant.add(new UserInfo(part.getString("name"), part.getString("photo"), part.getString("token")));
                        }
                        return;
                    } catch (JSONException e) {
                        return;
                    }

                }
            });
        }
    };

    private Emitter.Listener onExitInfo = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        return;
                    }
                    addMessage(new UserInfo(username, "", ""), "", 3);

                }
            });
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.emit("exit", token, roomID);
        mSocket.disconnect();
        mSocket.off("new message", onNewMessage);
    }
}
