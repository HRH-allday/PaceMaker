package com.example.q.pacemaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton add_goal = (FloatingActionButton) findViewById(R.id.main_add_goal);

        add_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GoalActivity.class);
                startActivity(intent);
            }
        });
        FloatingActionButton community = (FloatingActionButton) findViewById(R.id.main_community);

        community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommunityGoals.class);
                startActivity(intent);
            }
        });

    }
}
