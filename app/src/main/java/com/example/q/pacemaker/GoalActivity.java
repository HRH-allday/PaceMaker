package com.example.q.pacemaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.q.pacemaker.Utilities.RoundedImageView;
import com.example.q.pacemaker.Utilities.SendJSON;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.q.pacemaker.MainActivity.myUserInfo;
import static com.example.q.pacemaker.R.id.todo;

/**
 * Created by q on 2017-01-14.
 */

public class GoalActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    private ArrayList<CustomizeData> customizeDatas = new ArrayList<>();
    private CardView todoView;
    private CardView routineView;


    public static JSONArray mTodoList = new JSONArray();
    public static JSONArray mMondayList = new JSONArray();
    public static JSONArray mTuesdayList = new JSONArray();
    public static JSONArray mWednesdayList = new JSONArray();
    public static JSONArray mThursdayList = new JSONArray();
    public static JSONArray mFridayList = new JSONArray();
    public static JSONArray mSaturdayList = new JSONArray();
    public static JSONArray mSundayList = new JSONArray();
    public static JSONArray mMemoList = new JSONArray();

    public String title;
    public String description;
    private String cloneID;
    private String token;
    private String photoUrl;
    private String pid;



    public TextView todoTextView;
    public EditText todoEdit;
    public Button todoButton;
    public RecyclerView todoRecyclerView;
    public RecyclerView.LayoutManager mLayoutManager;
    public RecyclerView.Adapter todoAdapter;

    public TabLayout tabLayout;
    public RoutineAdapter adapter;
    public ViewPager viewPager;

    public Button memo_plus;
    public EditText memoEditView;
    public RecyclerView memoRecyclerView;
    public RecyclerView.Adapter memoAdapter;
    public RecyclerView.LayoutManager memoLayoutManager;

    private Button shareButton;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        token = FirebaseInstanceId.getInstance().getToken();

        Intent intent = getIntent();
        cloneID = intent.getStringExtra("cid");

        ArrayList<TodoListData> todoLists = new ArrayList<>();
        ArrayList<ArrayList<TodoListData>> mRoutineList = new ArrayList<>();
        ArrayList<TodoListData> memoLists = new ArrayList<>();

        ArrayList<String> titleLists = new ArrayList<>();
        ArrayList<String> cidLists = new ArrayList<>();


        try {
            JSONObject req = new JSONObject();
            req.put("token", token);

            JSONObject res = new SendJSON(App.server_url + App.routing_user_info, req.toString(), App.JSONcontentsType).execute().get();
            if (res != null && res.has("result") && res.getString("result").equals("success")) {
                JSONObject userData = res.getJSONObject("user");
                Log.i("userData", userData.toString());
                JSONArray titles = userData.getJSONArray("goals_title");
                JSONArray cids = userData.getJSONArray("goals_id");
                for(int i = 0; i < titles.length() ; i++){
                    titleLists.add(titles.getString(i));
                    cidLists.add(cids.getString(i));
                }
            }
        }catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        try {
            JSONObject req = new JSONObject();
            req.put("cid", cloneID);

            JSONObject res = new SendJSON(App.server_url + App.routing_goal_clone_info, req.toString(), App.JSONcontentsType).execute().get();
            if (res != null && res.has("result") && res.getString("result").equals("success")) {
                Log.i("clone", res.toString());
                JSONObject cloneData = res.getJSONObject("clone");
                title = cloneData.getString("title");
                pid = cloneData.getString("pid");
                mMondayList = cloneData.getJSONArray("mon");
                mTuesdayList = cloneData.getJSONArray("tue");
                mWednesdayList = cloneData.getJSONArray("wed");
                mThursdayList = cloneData.getJSONArray("thu");
                mFridayList = cloneData.getJSONArray("fri");
                mSaturdayList = cloneData.getJSONArray("sat");
                mSundayList = cloneData.getJSONArray("sun");
                photoUrl = cloneData.getString("photo");
                Log.i("photo", photoUrl);
                mMemoList = cloneData.getJSONArray("memo");
                mTodoList = cloneData.getJSONArray("todo");
                for(int i = 0; i < mTodoList.length() ; i++){
                    todoLists.add(new TodoListData(mTodoList.getString(i), "#ff1616"));
                }
                for(int i = 0; i < mMemoList.length() ; i++){
                    memoLists.add(new TodoListData(mMemoList.getString(i), "#ff1616"));
                }
                ArrayList<TodoListData> tlds = new ArrayList<>();
                for(int i = 0; i < mMondayList.length() ; i++){
                    tlds.add(new TodoListData(mMondayList.getString(i), "#ff1616"));
                }
                mRoutineList.add(tlds);
                tlds = new ArrayList<>();
                for(int i = 0; i < mTuesdayList.length() ; i++){
                    tlds.add(new TodoListData(mTuesdayList.getString(i), "#ff1616"));
                }
                mRoutineList.add(tlds);
                tlds = new ArrayList<>();
                for(int i = 0; i < mWednesdayList.length() ; i++){
                    tlds.add(new TodoListData(mWednesdayList.getString(i), "#ff1616"));
                }
                mRoutineList.add(tlds);
                tlds = new ArrayList<>();
                for(int i = 0; i < mThursdayList.length() ; i++){
                    tlds.add(new TodoListData(mThursdayList.getString(i), "#ff1616"));
                }
                mRoutineList.add(tlds);
                tlds = new ArrayList<>();
                for(int i = 0; i < mFridayList.length() ; i++){
                    tlds.add(new TodoListData(mFridayList.getString(i), "#ff1616"));
                }
                mRoutineList.add(tlds);
                tlds = new ArrayList<>();
                for(int i = 0; i < mSaturdayList.length() ; i++){
                    tlds.add(new TodoListData(mSaturdayList.getString(i), "#ff1616"));
                }
                mRoutineList.add(tlds);
                tlds = new ArrayList<>();
                for(int i = 0; i < mSundayList.length() ; i++){
                    tlds.add(new TodoListData(mSundayList.getString(i), "#ff1616"));
                }
                mRoutineList.add(tlds);
            }
        }catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        //Imageview setting
        ImageView goalCloneImage = (ImageView) findViewById(R.id.goal_clone_image);
        Picasso.with(getApplicationContext()).load(photoUrl).into(goalCloneImage);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(title);


        // nav drawer setting
        NavigationView navigationView = (NavigationView) findViewById(R.id.main_nav_list);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    case R.id.go_main:
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.go_community:
                        intent = new Intent(getApplicationContext(), CommunityGoals.class);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        break;

                }
                return false;

            }

        });

        View viewNavHeader = navigationView.getHeaderView(0);
        RoundedImageView profileImage = (RoundedImageView) viewNavHeader.findViewById(R.id.profile_image);
        Picasso.with(getApplicationContext()).load(myUserInfo.url).into(profileImage);
        TextView userName = (TextView) viewNavHeader.findViewById(R.id.username_profile);
        userName.setText(myUserInfo.userName);

        final Menu menu = navigationView.getMenu();
        final SubMenu subMenu = menu.addSubMenu("나의 목표");
        for (int i = 0; i < titleLists.size() ; i++) {
            Intent intentNav = new Intent(getApplicationContext(), GoalActivity.class);
            intent.putExtra("cid", cidLists.get(i));
            subMenu.add(titleLists.get(i)).setIcon(R.drawable.ic_done).setIntent(intentNav);
        }





        // todo card
        todoView = (CardView) findViewById(R.id.cardview);
        todoEdit = (EditText) findViewById(R.id.todo_edit);
        todoButton = (Button) findViewById(R.id.todo_button);
        todoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todo = todoEdit.getText().toString().trim();
                if(todo.equals("")) return;
                else{
                    todoEdit.setText("");
                    mTodoList.put(todo);
                    try {
                        JSONObject req = new JSONObject();
                        req.put("new_todo", todo);
                        req.put("cid", cloneID);

                        JSONObject res = new SendJSON(App.server_url + App.routing_add_todo, req.toString(), App.JSONcontentsType).execute().get();
                        if (res != null && res.has("result") && res.getString("result").equals("success")) {
                            ((TodoListAdapter) todoAdapter).addItem(new TodoListData(todo, "#ff1616"), 0);
                        }
                    }catch (JSONException | InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        todoTextView = (TextView) findViewById(todo);
        todoRecyclerView = (RecyclerView)  findViewById(R.id.todoRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        todoRecyclerView.setLayoutManager(mLayoutManager);
        todoTextView.setText("오늘의 할 일");
        todoAdapter = new TodoListAdapter(todoLists);
        todoRecyclerView.setAdapter(todoAdapter);

        // routine card

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
        adapter = new RoutineAdapter(getSupportFragmentManager(), mRoutineList.get(0),mRoutineList.get(1),mRoutineList.get(2),mRoutineList.get(3),mRoutineList.get(4),mRoutineList.get(5),mRoutineList.get(6), 1, cloneID);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(this);

        // memo card
        todoView = (CardView) findViewById(R.id.cardview);
        memo_plus = (Button) findViewById(R.id.memo_button);
        memoEditView = (EditText) findViewById(R.id.memo_edit);
        memoRecyclerView = (RecyclerView)  findViewById(R.id.memoRecyclerView);
        memoLayoutManager = new LinearLayoutManager(this);
        memoRecyclerView.setLayoutManager(memoLayoutManager);
        memoAdapter = new TodoListAdapter(memoLists);
        memoRecyclerView.setAdapter(memoAdapter);

        String memo;
        memo_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edit = memoEditView.getText().toString().trim();
                if(edit.equals(""))
                    return;
                else {
                    memoEditView.setText("");
                    mMemoList.put(edit);
                    try {
                        JSONObject req = new JSONObject();
                        req.put("new_memo", edit);
                        req.put("cid", cloneID);

                        JSONObject res = new SendJSON(App.server_url + App.routing_add_memo, req.toString(), App.JSONcontentsType).execute().get();
                        if (res != null && res.has("result") && res.getString("result").equals("success")) {
                            ((TodoListAdapter) memoAdapter).addItem(new TodoListData(edit, "#ff1616"), 0);
                        }
                    }catch (JSONException | InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        //share
        shareButton = (Button) findViewById(R.id.share_button);
        title = "제목";
        description = "설명";

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msg = new Intent(Intent.ACTION_SEND);
                msg.addCategory(Intent.CATEGORY_DEFAULT);
                msg.putExtra(Intent.EXTRA_TEXT, description);
                msg.putExtra(Intent.EXTRA_SUBJECT, title);
                msg.setType("text/plain");
                startActivity(Intent.createChooser(msg, "공유"));

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.invisible_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.go_community_clone:
                Intent intent = new Intent(getApplicationContext(), CommunityActivity.class);
                intent.putExtra("pid", pid);
                startActivity(intent);
                return true;
            case R.id.go_goal_info:
                return true;
            case R.id.delete_clone:
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
