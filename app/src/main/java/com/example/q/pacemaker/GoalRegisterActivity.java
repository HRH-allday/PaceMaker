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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;


public class GoalRegisterActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMapClickListener,
        OnMapReadyCallback {

    private static final String TAG = "@@@";
    private static final int SELECT_PHOTO = 1;
    private static final int SELECT_LOCATION = 2;

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

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.register_map_view);

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



        mMondayList = new JSONArray();
        mTuesdayList = new JSONArray();
        mWednesdayList = new JSONArray();
        mThursdayList = new JSONArray();
        mFridayList = new JSONArray();
        mSatdayList = new JSONArray();
        mSundayList = new JSONArray();


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
        routineAdapter = new RoutineAdapter(getSupportFragmentManager(), new ArrayList<TodoListData>(),new ArrayList<TodoListData>(),new ArrayList<TodoListData>(),new ArrayList<TodoListData>(),new ArrayList<TodoListData>(),new ArrayList<TodoListData>(),new ArrayList<TodoListData>(), 0, "1");
        viewPager.setAdapter(routineAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mapFragment.getMapAsync(this);

        try {
            base64EncodedImage = new Base64EncodeImage().execute(selectedImage).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
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

        Marker selectedMarker = googleMap.addMarker(new MarkerOptions().position(selectedPoint));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(selectedPoint));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        googleMap.setOnMapClickListener(GoalRegisterActivity.this);
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
                req.put("latitude", latitude);
                req.put("longitude", longitude);

                JSONObject res = new SendJSON(App.server_url + App.routing_new_goal_register, req.toString(), App.JSONcontentsType).execute().get();
                if (res != null && res.has("result") && res.getString("result").equals("success")) {
                    Log.i("register result : " , res.toString());
                    String cloneID = res.getString("cid");
                    Intent intent = new Intent(getApplicationContext(), GoalActivity.class);
                    intent.putExtra("cid", cloneID);
                    startActivity(intent);
                    finish();
                }

            } catch (JSONException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        register_photo.setImageBitmap(selectedImage);
                        base64EncodedImage = new Base64EncodeImage().execute(selectedImage).get();
                    } catch (FileNotFoundException | InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                break;

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
                    mapFragment.getMapAsync(GoalRegisterActivity.this);
                }

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
