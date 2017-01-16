package com.example.q.pacemaker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.q.pacemaker.Utilities.Base64EncodeImage;
import com.example.q.pacemaker.Utilities.SendJSON;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;


public class GoalRegisterActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    private static final int SELECT_PHOTO = 1;

    private TextView register_new_goal;
    private TextView register_goal_name_label;
    private TextView register_keyword_label;
    private TextView register_public_label;
    private TextView register_description_label;
    private TextView register_start_date_label;
    private TextView register_end_date_label;

    private ImageView register_photo;
    private EditText register_title;
    private AutoCompleteTextView register_keyword;
    private RadioGroup register_public;
    private RadioButton register_radio_public;
    private RadioButton register_radio_private;
    private EditText register_description;
    private EditText register_start_date;
    private EditText register_end_date;
    private Button register_button;

    private String[] keyword_hash = {"nodejs", "reactjs", "angularjs", "unity", "android", "machine learning", "TOEFL", "TEPS", "TOEIC"};
    private Bitmap selectedImage;
    private String base64EncodedImage;
    private Activity activity;

    private CardView routineView;
    public TabLayout tabLayout;
    public RoutineAdapter routineAdapter;
    public ViewPager viewPager;

    public static JSONArray mMondayList = new JSONArray();
    public static JSONArray mTuesdayList = new JSONArray();
    public static JSONArray mWednesdayList = new JSONArray();
    public static JSONArray mThursdayList = new JSONArray();
    public static JSONArray mFridayList = new JSONArray();
    public static JSONArray mSatdayList = new JSONArray();
    public static JSONArray mSundayList = new JSONArray();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goalregister);
        activity = this;

        register_new_goal = (TextView) findViewById(R.id.register_new_goal);
        register_goal_name_label = (TextView) findViewById(R.id.register_goal_name_label);
        register_keyword_label = (TextView) findViewById(R.id.register_keyword_label);
        register_public_label = (TextView) findViewById(R.id.register_public_label);
        register_description_label = (TextView) findViewById(R.id.register_description_label);
        register_start_date_label = (TextView) findViewById(R.id.register_start_date_label);
        register_end_date_label = (TextView) findViewById(R.id.register_end_date_label);

        register_photo = (ImageView) findViewById(R.id.register_photo);
        register_title = (EditText) findViewById(R.id.register_goal_name);
        register_keyword = (AutoCompleteTextView) findViewById(R.id.register_hashtag_keyword);
        register_public = (RadioGroup) findViewById(R.id.register_radio_group);
        register_radio_public = (RadioButton) findViewById(R.id.register_radio_public);
        register_radio_private = (RadioButton) findViewById(R.id.register_radio_private);
        register_description = (EditText) findViewById(R.id.register_description);
        register_start_date = (EditText) findViewById(R.id.register_start_date);
        register_end_date = (EditText) findViewById(R.id.register_end_date);
        register_button = (Button) findViewById(R.id.register_goal_button);

        register_photo.setOnClickListener(registerPhotoOnClicked);
        register_start_date.setOnClickListener(startDateOnClicked);
        register_end_date.setOnClickListener(endDateOnClicked);
        register_button.setOnClickListener(registerButtonOnClicked);

        register_new_goal.setTypeface(App.NanumBarunGothicBold);
        register_goal_name_label.setTypeface(App.NanumBarunGothicBold);
        register_keyword_label.setTypeface(App.NanumBarunGothicBold);
        register_public_label.setTypeface(App.NanumBarunGothicBold);
        register_description_label.setTypeface(App.NanumBarunGothicBold);
        register_start_date_label.setTypeface(App.NanumBarunGothicBold);
        register_end_date_label.setTypeface(App.NanumBarunGothicBold);

        register_title.setTypeface(App.NanumBarunGothic);
        register_keyword.setTypeface(App.NanumBarunGothic);
        register_radio_public.setTypeface(App.NanumBarunGothic);
        register_radio_private.setTypeface(App.NanumBarunGothic);
        register_description.setTypeface(App.NanumBarunGothic);
        register_start_date.setTypeface(App.NanumBarunGothic);
        register_end_date.setTypeface(App.NanumBarunGothic);
        register_button.setTypeface(App.NanumBarunGothic);





        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, keyword_hash);
        register_keyword.setAdapter(adapter);

        selectedImage = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);

        //routine setting
        routineView = (CardView) findViewById(R.id.routine_cardview);
        tabLayout = (TabLayout) findViewById(R.id.routine_tablayout);

        tabLayout.addTab(tabLayout.newTab().setText("월"));
        tabLayout.addTab(tabLayout.newTab().setText("화"));
        tabLayout.addTab(tabLayout.newTab().setText("수"));
        tabLayout.addTab(tabLayout.newTab().setText("목"));
        tabLayout.addTab(tabLayout.newTab().setText("금"));
        tabLayout.addTab(tabLayout.newTab().setText("토"));
        tabLayout.addTab(tabLayout.newTab().setText("일"));

        viewPager = (ViewPager) findViewById(R.id.routine_viewpager);
        routineAdapter = new RoutineAdapter(getSupportFragmentManager(), new ArrayList<TodoListData>(),new ArrayList<TodoListData>(),new ArrayList<TodoListData>(),new ArrayList<TodoListData>(),new ArrayList<TodoListData>(),new ArrayList<TodoListData>(),new ArrayList<TodoListData>(), 0);
        viewPager.setAdapter(routineAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(this);




        try {
            base64EncodedImage = new Base64EncodeImage().execute(selectedImage).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }



    View.OnClickListener registerPhotoOnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }
    };

    View.OnClickListener startDateOnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog mDatePicker = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month += 1;
                    String dateFrom = year + "-" + formatMonth(String.valueOf(month)) + "-" + formatMonth(String.valueOf(dayOfMonth));
                    register_start_date.setText(year + "년 " + (month) + "월 " + dayOfMonth + "일");
                    Log.e("formattedDateMK", dateFrom);
                }
            }, year, month, day);
            mDatePicker.show();
        }

        private String formatMonth(String month){
            if (Integer.parseInt(month) < 10)
                return (0+month);
            else
                return month;
        }
    };

    View.OnClickListener endDateOnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog mDatePicker = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month += 1;
                    String dateTo = year + "-" + formatMonth(String.valueOf(month)) + "-" + formatMonth(String.valueOf(dayOfMonth));
                    register_end_date.setText(year + "년 " + (month) + "월 " + dayOfMonth + "일");
                    Log.e("formattedDateMK", dateTo);
                }
            }, year, month, day);
            mDatePicker.show();
        }

        private String formatMonth(String month){
            if (Integer.parseInt(month) < 10)
                return (0+month);
            else
                return month;
        }
    };

    View.OnClickListener registerButtonOnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // title, description, dateFrom, dateTo, public
            String title = register_title.getText().toString();
            String description = register_description.getText().toString();
            Boolean public_ = register_radio_public.isChecked();
            String dateFrom = register_start_date.getText().toString();
            String dateTo = register_end_date.getText().toString();

            try {
                JSONObject req = new JSONObject();
                req.put("title", title);
                req.put("description", description);
                req.put("public", public_);
                req.put("dateFrom", dateFrom);
                req.put("dateTo", dateTo);
                req.put("photo", base64EncodedImage);
                req.put("mon", mMondayList);
                req.put("tue", mTuesdayList);
                req.put("wed", mWednesdayList);
                req.put("thu", mThursdayList);
                req.put("fri", mFridayList);
                req.put("sat", mSatdayList);
                req.put("sun", mSundayList);
                req.put("token", FirebaseInstanceId.getInstance().getToken());

                JSONObject res = new SendJSON(App.server_url + App.routing_new_goal_register, req.toString(), App.JSONcontentsType).execute().get();
                if (res != null && res.has("result") && res.getString("result").equals("success")) {
                    String cloneID = res.getString("cid");
                    Intent intent = new Intent(getApplicationContext(), GoalActivity.class);
                    intent.putExtra("cloneID", cloneID);
                    startActivity(intent);
                    finish();
                }

            } catch (JSONException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mMondayList = new JSONArray();
        mTuesdayList = new JSONArray();
        mWednesdayList = new JSONArray();
        mThursdayList = new JSONArray();
        mFridayList = new JSONArray();
        mSatdayList = new JSONArray();
        mSundayList = new JSONArray();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        register_photo.setImageBitmap(selectedImage);
                        base64EncodedImage = new Base64EncodeImage().execute(selectedImage).get();
                    } catch (FileNotFoundException | InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
