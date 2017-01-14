package com.example.q.pacemaker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

/**
 * Created by q on 2017-01-14.
 */

public class GoalRegisterActivity extends Activity {
    private String[] keyword_hash = {"nodejs", "reactjs", "angularjs", "unity", "android", "machine learning", "TOEFL", "TEPS", "TOEIC"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goalregister);

        AutoCompleteTextView hashView = (AutoCompleteTextView) findViewById(R.id.hashtag_keyword);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, keyword_hash);
        hashView.setAdapter(adapter);
    }
}
