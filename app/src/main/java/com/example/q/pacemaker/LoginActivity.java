package com.example.q.pacemaker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.q.pacemaker.Utilities.Base64EncodeImage;
import com.example.q.pacemaker.Utilities.RoundedImageView;
import com.example.q.pacemaker.Utilities.SendJSON;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    private static final int SELECT_PHOTO = 1;

    private ImageView user_photo;
    private EditText user_name;
    private TextView login_message;
    private Button login_cancel;
    private Button login_continue;

    private String token, base64EncodedImage;
    private Bitmap bmp_user_photo;
    private String login_msg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check the shared preference
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        token = pref.getString("token", "");
        if (token.equals("")) {
            // If there is no token in shared preference, get token from firebase instance and store
            SharedPreferences.Editor editor = pref.edit();
            token = FirebaseInstanceId.getInstance().getToken();
            editor.putString("token", token);
            editor.apply();

            // Also save it to App.token
            App.token = token;

        } else {
            // If there is token, update App.token
            App.token = token;
        }

        // Send the token to the server and check whether the user exists or not
        boolean isRegistered = false;
        String user_name_from_server = "";
        try {
            JSONObject req = new JSONObject();
            req.put("token", token);

            JSONObject res = new SendJSON(App.server_url + App.routing_user_login, req.toString(), App.JSONcontentsType).execute().get();

            if (res != null && res.has("result")) {
                Log.e("LOGIN_RES", res.toString());
                user_name_from_server = res.getJSONObject("obj").getString("name");
                isRegistered = res.getString("result").equals("success");
            }
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (isRegistered) {
            // if the user exists in server db, skip the register view and go to the main
            Toast.makeText(getApplicationContext(), user_name_from_server + "님으로 로그인했습니다.", Toast.LENGTH_SHORT).show();
            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        } else {
            // if the user does not exist in server db, show the register view
            // name, photo, e-mail(?),
            setContentView(R.layout.activity_login);

            user_photo = (ImageView) findViewById(R.id.login_user_photo);
            user_name = (EditText) findViewById(R.id.login_user_name);
            login_cancel = (Button) findViewById(R.id.login_cancel);
            login_continue = (Button) findViewById(R.id.login_continue);
            login_message = (TextView) findViewById(R.id.user_login_message);

            user_photo.setOnClickListener(userPhotoOnClicked);
            login_cancel.setOnClickListener(loginCancelOnClicked);
            login_continue.setOnClickListener(loginContinueOnClicked);

            user_name.setTypeface(App.NanumBarunGothicBold);
            login_message.setTypeface(App.NanumBarunGothicLight);
            login_cancel.setTypeface(App.NanumBarunGothic);
            login_continue.setTypeface(App.NanumBarunGothic);

            // default photo
            Bitmap default_user_photo = BitmapFactory.decodeResource(getResources(), R.drawable.default_user);
            try {
                base64EncodedImage = new Base64EncodeImage().execute(default_user_photo).get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            default_user_photo = RoundedImageView.getCroppedBitmap(default_user_photo, default_user_photo.getWidth());
            user_photo.setImageBitmap(default_user_photo);

            // login message
            user_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    login_msg = user_name.getText().toString() + "님으로 계속 하시겠습니까?";
                    login_message.setText(login_msg);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    View.OnClickListener userPhotoOnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }
    };

    View.OnClickListener loginCancelOnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            System.exit(0);
        }
    };

    View.OnClickListener loginContinueOnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                JSONObject req = new JSONObject();
                req.put("name", user_name.getText().toString());
                req.put("photo", base64EncodedImage);
                req.put("goals_title", new JSONArray());
                req.put("goals_id", new JSONArray());
                req.put("token", token);

                JSONObject res = new SendJSON(App.server_url + App.routing_user_register, req.toString(), App.JSONcontentsType).execute().get();
                if (res != null && res.has("result") && res.getString("result").equals("success")) {
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            } catch (JSONException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        bmp_user_photo = BitmapFactory.decodeStream(imageStream);
                        base64EncodedImage = new Base64EncodeImage().execute(bmp_user_photo).get();
                        bmp_user_photo = RoundedImageView.getCroppedBitmap(bmp_user_photo, bmp_user_photo.getWidth());
                        user_photo.setImageBitmap(bmp_user_photo);
                    } catch (FileNotFoundException | InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                break;

            default:
                break;
        }
    }
}
