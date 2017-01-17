package com.example.q.pacemaker;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.example.q.pacemaker.Adapters.GoalAdapter;
import com.example.q.pacemaker.Utilities.Base64EncodeImage;
import com.example.q.pacemaker.Utilities.SendJSON;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import java.util.Calendar;
import java.util.concurrent.ExecutionException;


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

    private CardView description_routine_cardview;
    public TabLayout description_routine_tablayout;
    public RoutineAdapter description_routineAdapter;
    public ViewPager description_routine_viewpager;

    public static JSONArray mMondayList = new JSONArray();
    public static JSONArray mTuesdayList = new JSONArray();
    public static JSONArray mWednesdayList = new JSONArray();
    public static JSONArray mThursdayList = new JSONArray();
    public static JSONArray mFridayList = new JSONArray();
    public static JSONArray mSatdayList = new JSONArray();
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

        description_photo = (ImageView) findViewById(R.id.description_photo);
        description_goal_name = (TextView) findViewById(R.id.description_goal_name);
        description_period = (TextView) findViewById(R.id.description_period);
        description_people = (TextView) findViewById(R.id.description_people);
        description_description = (TextView) findViewById(R.id.description_description);
        description_join_button = (Button) findViewById(R.id.description_join_button);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.description_map_view);

        //routine setting
        description_routine_cardview = (CardView) findViewById(R.id.description_routine_cardview);
        description_routine_tablayout = (TabLayout) findViewById(R.id.description_routine_tablayout);

        description_routine_viewpager = (ViewPager) findViewById(R.id.description_routine_viewpager);
        description_routineAdapter = new RoutineAdapter(getSupportFragmentManager(), new ArrayList<TodoListData>(),new ArrayList<TodoListData>(),new ArrayList<TodoListData>(),new ArrayList<TodoListData>(),new ArrayList<TodoListData>(),new ArrayList<TodoListData>(),new ArrayList<TodoListData>(), 0, "1");

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mapFragment.getMapAsync(this);
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

        goalImageUrl = "http://52.78.200.87:3000/static/images/img_148456074849257bc49d4c52527e08b0bae46d666f858.jpeg";
        Picasso.with(activity.getApplicationContext()).load(goalImageUrl).into(description_photo);

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

        mMondayList = new JSONArray();
        mTuesdayList = new JSONArray();
        mWednesdayList = new JSONArray();
        mThursdayList = new JSONArray();
        mFridayList = new JSONArray();
        mSatdayList = new JSONArray();
        mSundayList = new JSONArray();
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
            Intent intent = new Intent(activity, GoalActivity.class);
            // intent.putExtra("<field_name>", goalObj.toString());
            activity.startActivity(intent);
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
