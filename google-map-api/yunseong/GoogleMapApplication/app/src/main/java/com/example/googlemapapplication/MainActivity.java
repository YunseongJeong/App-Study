package com.example.googlemapapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback { //implements 중요

    private FragmentManager fragmentManager;
    private MapFragment mapFragment;

    private Thread thread;

    private LocationManager locationManager;
    Location location;
    double lattitude;
    double longitude;

    private Boolean isFine = false;

    private RelativeLayout rootLayout;
    private LinearLayout linearLayout;
    private Button btn_toMyLocation;

    private EditText et_name;

    private Button btnForApply;
    private boolean isForApply = true;
    private LatLng latLngForMarker;

    GoogleMap map;
    private boolean onMarker = true;
    Marker marker;
    Marker markerForRetouch;

    private final static String TAG = "MainActivity.java";

    private final static int UPDATELOCATION = 0;

    private int mpHeight;

    private int lastY, curY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TedPermission.with(this) //권한을 얻기 위한 코드이다.
                .setPermissionListener(permission)
                .setRationaleMessage("위치 확인를 위하여 권한을 허용해주세요.")
                .setDeniedMessage("권한이 거부되었습니다. 설정 > 권한에서 허용할 수 있습니다.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();

        fragmentManager = getFragmentManager();
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);

        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);

        et_name = (EditText)findViewById(R.id.et_name);

        btnForApply = (Button)findViewById(R.id.btnForApply);
        btnForApply.setOnClickListener(v -> {
            if (isForApply) {
                if (et_name.getText().toString() == "") {
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title(et_name.getText().toString());
                    markerOptions.position(latLngForMarker);
                    map.addMarker(markerOptions);
                    isForApply = false;
                    btnForApply.setText("수정");
                }
            } else {
                markerForRetouch.setTitle(et_name.getText().toString());
            }
        });

        btn_toMyLocation = (Button)findViewById(R.id.btn_toMyLocation);
        btn_toMyLocation.setOnClickListener(v -> {
            onMarker = true;
            btn_toMyLocation.setVisibility(View.INVISIBLE);
            Log.d(TAG, ("onClickListener : onMarker = " + onMarker));
            LatLng location = new LatLng(lattitude, longitude);
            map.moveCamera(CameraUpdateFactory.newLatLng(location));
        });

        thread = new Thread() {
            @SuppressLint("MissingPermission")
            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isFine) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        lattitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                    handler.sendEmptyMessage(UPDATELOCATION);
                    Log.d(TAG, "run : threadIsRunning");
                }
            }
        };
        thread.start();

        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
                int action = event.getAction();
                if(action == MotionEvent.ACTION_DOWN){
                    lastY = (int)event.getY() + layoutParams.topMargin;
                } else if (action == MotionEvent.ACTION_MOVE && Math.abs(lastY - layoutParams.topMargin) < 100 && lastY < (mpHeight - 200) && lastY > 100) {
                    curY = (int)event.getY() + layoutParams.topMargin;
                    layoutParams.setMargins(0, (layoutParams.topMargin +curY-lastY), 0, 0);
                    linearLayout.setLayoutParams(layoutParams);
                    lastY = curY;
                }
                return true;
            }
        });
    }

    @Override
    public void onMapReady(@NonNull @org.jetbrains.annotations.NotNull GoogleMap googleMap) {
        LatLng location = new LatLng(35.231004, 129.082277); //지도에 marker을 만들기 위한 좌표

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("현위치");
        markerOptions.draggable(true);
        markerOptions.position(location);

        marker = googleMap.addMarker(markerOptions); //위에서 설정한 marker을 적용

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16)); //marker 위치로 camera를 이동하고 줌함

        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {

                Log.d(TAG, "onCameraMove : TouchLinstenerIsRunning");

                if (Math.abs(map.getCameraPosition().target.latitude - lattitude) < 0.0005 && Math.abs(map.getCameraPosition().target.longitude - longitude) < 0.0005) {
                    onMarker = true;
                    btn_toMyLocation.setVisibility(View.INVISIBLE);
                    Log.d(TAG, ("onCameraMove : onMarker = " + onMarker));
                } else {
                    onMarker = false;
                    btn_toMyLocation.setVisibility(View.VISIBLE);
                    Log.d(TAG, ("onCameraMove : onMarker = " + onMarker));
                }
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) { //질문
                if (marker.getTitle().toString() != "현위치"){
                    markerForRetouch = marker;
                    et_name.setText(marker.getTitle());
                    onMarker = false;
                    btn_toMyLocation.setVisibility(View.VISIBLE);
                } else  {
                    onMarker = true;
                    btn_toMyLocation.setVisibility(View.INVISIBLE);
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude)));

                return true;
            }
        });

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
                isForApply = true;
                btnForApply.setText("생성");
                layoutParams.setMargins(0, mpHeight/2+100, 0, 0);
                linearLayout.setLayoutParams(layoutParams);
                btn_toMyLocation.setVisibility(View.VISIBLE);
                onMarker = false;
                latLngForMarker = latLng;
            }
        });
        map = googleMap;
    }

    PermissionListener permission = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(MainActivity.this, "권한 허가", Toast.LENGTH_SHORT);
            locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
            isFine = true;
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(MainActivity.this, "권한 거부", Toast.LENGTH_SHORT);
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == UPDATELOCATION){
                LatLng location = new LatLng(lattitude, longitude);
                marker.setPosition(location);
                Log.d(TAG, ("handleMessage: onMarker = " + onMarker));
                if (onMarker) map.moveCamera(CameraUpdateFactory.newLatLng(location));
                //Toast.makeText(getApplicationContext(), ("lattitude : " + lattitude + " longitude : "+ longitude), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mpHeight = rootLayout.getHeight();
    }
}