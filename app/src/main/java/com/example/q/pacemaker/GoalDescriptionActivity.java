package com.example.q.pacemaker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.q.pacemaker.Utilities.RoundedImageView;
import com.example.q.pacemaker.Utilities.SendJSON;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.q.pacemaker.MainActivity.cidList;
import static com.example.q.pacemaker.MainActivity.myUserInfo;
import static com.example.q.pacemaker.MainActivity.titleList;


public class GoalDescriptionActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback {

    private static final String TAG = "@@@";

    private ImageView description_photo;
    private TextView description_goal_name;
    private TextView description_period;
    private TextView description_people;
    private TextView description_description;
    private Button description_join_button;

    private String goalImageUrl;
    private Bitmap goalImage;
    private Activity activity;

    private String token;
    private String photoUrl;
    private String pid;
    private String title;
    private int numParticipants;
    private String description;
    private String datefrom;
    private String dateto;


    private CardView description_routine_cardview;
    public TabLayout description_routine_tablayout;
    public RoutineAdapter description_routineAdapter;
    public ViewPager description_routine_viewpager;

    public static JSONArray mMondayList = new JSONArray();
    public static JSONArray mTuesdayList = new JSONArray();
    public static JSONArray mWednesdayList = new JSONArray();
    public static JSONArray mThursdayList = new JSONArray();
    public static JSONArray mFridayList = new JSONArray();
    public static JSONArray mSaturdayList = new JSONArray();
    public static JSONArray mSundayList = new JSONArray();

    SupportMapFragment mapFragment;
    LatLng selectedPoint = new LatLng(37.56, 126.97);
    private String latitude="37.56", longitude="126.97";
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient = null;
    private LocationRequest mLocationRequest;
    LocationManager locationManager;
    private static final int REQUEST_CODE_LOCATION = 2000;
    boolean setGPS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goaldescription);
        activity = this;
        token = FirebaseInstanceId.getInstance().getToken();

        Intent intentFrom = getIntent();
        pid = intentFrom.getStringExtra("pid");


        ArrayList<ArrayList<TodoListData>> mRoutineList = new ArrayList<>();

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
            req.put("pid", pid);

            JSONObject res = new SendJSON(App.server_url + App.routing_goal_info, req.toString(), App.JSONcontentsType).execute().get();
            if (res != null && res.has("result") && res.getString("result").equals("success")) {
                Log.i("goal", res.toString());
                JSONObject goalData = res.getJSONObject("goal");
                title = goalData.getString("title");
                description = goalData.getString("description");
                mMondayList = goalData.getJSONArray("mon");
                mTuesdayList = goalData.getJSONArray("tue");
                mWednesdayList = goalData.getJSONArray("wed");
                mThursdayList = goalData.getJSONArray("thu");
                mFridayList = goalData.getJSONArray("fri");
                mSaturdayList = goalData.getJSONArray("sat");
                mSundayList = goalData.getJSONArray("sun");
                photoUrl = goalData.getString("photo");
                latitude = goalData.getString("latitude");
                longitude = goalData.getString("longitude");
                numParticipants = goalData.getInt("numPeople");
                datefrom = goalData.getString("dateFrom");
                dateto = goalData.getString("dateTo");
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

        try {
            JSONObject req = new JSONObject();
            req.put("token", token);
            req.put("pid", pid);

            JSONObject res = new SendJSON(App.server_url + App.routing_check_register, req.toString(), App.JSONcontentsType).execute().get();
            if (res != null && res.has("result") && res.getString("result").equals("success")) {

            }
        }catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }





        description_photo = (ImageView) findViewById(R.id.description_photo);

        description_goal_name = (TextView) findViewById(R.id.description_goal_name);
        description_goal_name.setText(title);

        description_period = (TextView) findViewById(R.id.description_period);
        description_period.setText(datefrom + " ~ " + dateto);

        description_people = (TextView) findViewById(R.id.description_people);
        description_people.setText(numParticipants + "명 참여중");

        description_description = (TextView) findViewById(R.id.description_description);
        description_description.setText(description);

        description_join_button = (Button) findViewById(R.id.description_join_button);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.description_map_view);

        //routine setting
        description_routine_cardview = (CardView) findViewById(R.id.description_routine_cardview);
        description_routine_tablayout = (TabLayout) findViewById(R.id.description_routine_tablayout);

        description_routine_viewpager = (ViewPager) findViewById(R.id.description_routine_viewpager);
        description_routineAdapter =  new RoutineAdapter(getSupportFragmentManager(), mRoutineList.get(0),mRoutineList.get(1),mRoutineList.get(2),mRoutineList.get(3),mRoutineList.get(4),mRoutineList.get(5),mRoutineList.get(6), 2, "2");
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mapFragment.getMapAsync(this);



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
        for (int i = 0; i < titleList.size() ; i++) {
            Intent intentNav = new Intent(getApplicationContext(), GoalActivity.class);
            intentNav.putExtra("cid", cidList.get(i));
            subMenu.add(titleList.get(i)).setIcon(R.drawable.ic_done).setIntent(intentNav);
        }


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

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        selectedPoint = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        Marker selectedMarker = googleMap.addMarker(new MarkerOptions().position(selectedPoint));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(selectedPoint));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                Log.d( TAG, "onMapLoaded" );
                if (mGoogleApiClient == null)
                    buildGoogleApiClient();
            }
        });

        //구글 플레이 서비스 초기화
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
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
    public void onConnected(@Nullable Bundle bundle) {
        /*
        Log.d( TAG, "onConnected" );

        if ( locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            setGPS = true;

        mLocationRequest = new LocationRequest();
        //mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);


        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Log.d( TAG, "onConnected " + "getLocationAvailability mGoogleApiClient.isConnected()="+mGoogleApiClient.isConnected() );
            if ( !mGoogleApiClient.isConnected()  ) mGoogleApiClient.connect();


            // LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);

            if ( setGPS && mGoogleApiClient.isConnected() )//|| locationAvailability.isLocationAvailable() )
            {
                Log.d( TAG, "onConnected " + "requestLocationUpdates" );
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if ( location == null ) return;


                //현재 위치에 마커 생성
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("현재위치");
                // googleMap.addMarker(markerOptions);

                //지도 상에서 보여주는 영역 이동
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            }
        }
        */
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

        description_goal_name.setTypeface(App.NanumBarunGothicBold);
        description_period.setTypeface(App.NanumBarunGothicBold);
        description_people.setTypeface(App.NanumBarunGothicBold);
        description_description.setTypeface(App.NanumBarunGothicBold);
        description_join_button.setTypeface(App.NanumBarunGothicBold);

        description_join_button.setOnClickListener(joinButtonOnClicked);

        Picasso.with(getApplicationContext()).load(photoUrl).into(description_photo);

        description_routine_tablayout.addTab(description_routine_tablayout.newTab().setText("월"));
        description_routine_tablayout.addTab(description_routine_tablayout.newTab().setText("화"));
        description_routine_tablayout.addTab(description_routine_tablayout.newTab().setText("수"));
        description_routine_tablayout.addTab(description_routine_tablayout.newTab().setText("목"));
        description_routine_tablayout.addTab(description_routine_tablayout.newTab().setText("금"));
        description_routine_tablayout.addTab(description_routine_tablayout.newTab().setText("토"));
        description_routine_tablayout.addTab(description_routine_tablayout.newTab().setText("일"));

        description_routine_viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(description_routine_tablayout));
        description_routine_viewpager.setAdapter(description_routineAdapter);
        description_routine_tablayout.addOnTabSelectedListener(this);


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

            /*
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
            */

            mGoogleApiClient.disconnect();
            mGoogleApiClient = null;
        }

        super.onDestroy();
    }

    View.OnClickListener joinButtonOnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            try {
                JSONObject req = new JSONObject();
                req.put("token", token);
                req.put("pid", pid);

                JSONObject res = new SendJSON(App.server_url + App.routing_user_follow, req.toString(), App.JSONcontentsType).execute().get();
                if (res != null && res.has("result") && res.getString("result").equals("success")) {
                    Intent intent = new Intent(activity, GoalActivity.class);
                    intent.putExtra("cid", res.getString("cid"));
                    activity.startActivity(intent);

                }
            }catch (JSONException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            /*
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
                req.put("latitude", latitude);
                req.put("longitude", longitude);

                JSONObject res = new SendJSON(App.server_url + App.routing_new_goal_register, req.toString(), App.JSONcontentsType).execute().get();
                if (res != null && res.has("result") && res.getString("result").equals("success")) {
                    String cloneID = res.getString("cid");
                    Intent intent = new Intent(getApplicationContext(), GoalActivity.class);
                    intent.putExtra("cid", cloneID);
                    startActivity(intent);
                    finish();
                }

            } catch (JSONException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            */
        }
    };

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        description_routine_viewpager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
