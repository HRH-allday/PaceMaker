package com.example.q.pacemaker;

import android.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.example.q.pacemaker.Utilities.Base64EncodeImage;
import com.example.q.pacemaker.Utilities.RoundedImageView;
import com.example.q.pacemaker.Utilities.SendJSON;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.data.Goal;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.q.pacemaker.MainActivity.myUserInfo;
import static com.example.q.pacemaker.R.id.todo;

/**
 * Created by q on 2017-01-14.
 */

public class GoalActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMapClickListener,
        OnMapReadyCallback {
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

    private static final String TAG = "@@@";
    private static final int SELECT_PHOTO = 1;
    private static final int SELECT_LOCATION = 2;
    SupportMapFragment mapFragment;
    LatLng selectedPoint = new LatLng(37.56, 126.97);
    private String latitude="37.56", longitude="126.97";
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient = null;
    private LocationRequest mLocationRequest;
    LocationManager locationManager;
    private static final int REQUEST_CODE_LOCATION = 2000;
    private static final int REQUEST_CODE_GPS = 2001;
    boolean setGPS = false;

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
                latitude = cloneData.getString("latitude");
                longitude = cloneData.getString("longitude");
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

        // Maps
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.goal_map_fragment);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mapFragment.getMapAsync(this);

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

    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();


    }

    //GPS 활성화를 위한 다이얼로그 보여주기
    private void showGPSDisabledAlertToUser()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS가 비활성화 되어있습니다. 활성화 할까요?")
                .setCancelable(false)
                .setPositiveButton("설정", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(callGPSSettingIntent, REQUEST_CODE_GPS);
                    }
                });

        alertDialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        selectedPoint = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        Marker selectedMarker = googleMap.addMarker(new MarkerOptions().position(selectedPoint));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(selectedPoint));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        googleMap.setOnMapClickListener(GoalActivity.this);
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                Log.d( TAG, "onMapLoaded" );

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    checkLocationPermission();
                }
                else
                {

                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !setGPS)
                    {
                        Log.d(TAG, "onMapLoaded");
                        showGPSDisabledAlertToUser();
                    }

                    if (mGoogleApiClient == null) {
                        buildGoogleApiClient();
                    }
                    googleMap.setMyLocationEnabled(false);
                }

            }
        });

        //구글 플레이 서비스 초기화
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ActivityCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                buildGoogleApiClient();

                googleMap.setMyLocationEnabled(false);
            }
            else
            {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(selectedPoint));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
        }
        else
        {
            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(false);
        }
    }

    @Override
    public void onMapClick(LatLng point) {
        Intent selectLocationIntent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivityForResult(selectLocationIntent, SELECT_LOCATION);
    }

    public boolean checkLocationPermission()
    {
        Log.d( TAG, "checkLocationPermission");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                //퍼미션 요청을 위해 UI를 보여줘야 하는지 검사
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                    //Prompt the user once explanation has been shown;
                    requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);

                } else
                    //UI보여줄 필요 없이 요청
                    requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);

                return false;
            } else {

                Log.d( TAG, "checkLocationPermission"+"이미 퍼미션 획득한 경우");

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !setGPS)
                {
                    Log.d(TAG, "checkLocationPermission Version >= M");
                    showGPSDisabledAlertToUser();
                }

                if (mGoogleApiClient == null) {
                    Log.d( TAG, "checkLocationPermission "+"mGoogleApiClient==NULL");
                    buildGoogleApiClient();
                }
                else  Log.d( TAG, "checkLocationPermission "+"mGoogleApiClient!=NULL");

                if ( mGoogleApiClient.isConnected() ) Log.d( TAG, "checkLocationPermission"+"mGoogleApiClient 연결되 있음");
                else Log.d( TAG, "checkLocationPermission"+"mGoogleApiClient 끊어져 있음");


                mGoogleApiClient.reconnect();//이미 연결되 있는 경우이므로 다시 연결

                googleMap.setMyLocationEnabled(false);
            }
        }
        else {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !setGPS)
            {
                Log.d(TAG, "checkLocationPermission Version < M");
                showGPSDisabledAlertToUser();
            }

            if (mGoogleApiClient == null) {
                buildGoogleApiClient();
            }
            googleMap.setMyLocationEnabled(false);
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_LOCATION: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //퍼미션이 허가된 경우
                    if ((ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED))
                    {

                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !setGPS)
                        {
                            Log.d(TAG, "onRequestPermissionsResult");
                            showGPSDisabledAlertToUser();
                        }


                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        googleMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "퍼미션 취소", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d( TAG, "onConnected" );

        if ( locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            setGPS = true;

        mLocationRequest = new LocationRequest();
        //mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);


        if (ActivityCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Log.d( TAG, "onConnected " + "getLocationAvailability mGoogleApiClient.isConnected()="+mGoogleApiClient.isConnected() );
            if ( !mGoogleApiClient.isConnected()  ) mGoogleApiClient.connect();


            // LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);

            if ( setGPS && mGoogleApiClient.isConnected() )//|| locationAvailability.isLocationAvailable() )
            {
                Log.d( TAG, "onConnected " + "requestLocationUpdates" );
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if ( location == null ) return;

                /*
                //현재 위치에 마커 생성
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("현재위치");
                // googleMap.addMarker(markerOptions);

                //지도 상에서 보여주는 영역 이동
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            */
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        //구글 플레이 서비스 연결이 해제되었을 때, 재연결 시도
        Log.d(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onPause() {
        if ( mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {

        Log.d( TAG, "OnDestroy");

        if (mGoogleApiClient != null) {
            mGoogleApiClient.unregisterConnectionCallbacks(this);
            mGoogleApiClient.unregisterConnectionFailedListener(this);

            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }

            mGoogleApiClient.disconnect();
            mGoogleApiClient = null;
        }

        super.onDestroy();
    }


    @Override
    public void onLocationChanged(Location location) {

        /*
        String errorMessage = "";

        googleMap.clear();

        //현재 위치에 마커 생성
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("현재위치");
        googleMap.addMarker(markerOptions);

        //지도 상에서 보여주는 영역 이동
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        googleMap.getUiSettings().setCompassEnabled(true);

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        // Address found using the Geocoder.
        List<Address> addresses = null;

        try {
            // Using getFromLocation() returns an array of Addresses for the area immediately
            // surrounding the given latitude and longitude. The results are a best guess and are
            // not guaranteed to be accurate.
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, we get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = "지오코더 서비스 사용불가";
            Toast.makeText( this, errorMessage, Toast.LENGTH_LONG).show();
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = "잘못된 GPS 좌표";
            Toast.makeText( this, errorMessage, Toast.LENGTH_LONG).show();

        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "주소 미발견";
                Log.e(TAG, errorMessage);
            }
            Toast.makeText( this, errorMessage, Toast.LENGTH_LONG).show();
        } else {
            Address address = addresses.get(0);
            //Toast.makeText( this, address.getAddressLine(0).toString(), Toast.LENGTH_LONG).show();
        }
        */
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_LOCATION:
                if (resultCode == RESULT_OK) {
                    try {
                        String location = data.getData().toString();
                        String delim = "[/]";
                        String[] tokens = location.split(delim);
                        latitude = tokens[0];
                        longitude = tokens[1];

                        selectedPoint = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                        googleMap.clear();
                        Marker selectedMarker = googleMap.addMarker(new MarkerOptions().position(selectedPoint));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(selectedPoint));
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                        JSONObject jobj = new JSONObject();
                        jobj.put("cid", cloneID);
                        jobj.put("latitude", latitude);
                        jobj.put("longitude", longitude);

                        JSONObject res = new SendJSON(App.server_url + App.routing_update_clone_location, jobj.toString(), App.JSONcontentsType).execute().get();
                        if (res != null && res.has("result") && res.getString("result").equals("success")) {
                            Log.i("UPDATED", "Location");
                        }

                        Log.e("LOCATION : ", location);
                        Log.e("latitude", latitude);
                        Log.e("longitude", longitude);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            case  REQUEST_CODE_GPS:
                //Log.d(TAG,""+resultCode);
                //if (resultCode == RESULT_OK)
                //사용자가 GPS 활성 시켰는지 검사
                if ( locationManager == null)
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                if ( locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    // GPS 가 ON으로 변경되었을 때의 처리.
                    setGPS = true;
                    mapFragment.getMapAsync(GoalActivity.this);
                }

            default:
                break;
        }
    }
}
