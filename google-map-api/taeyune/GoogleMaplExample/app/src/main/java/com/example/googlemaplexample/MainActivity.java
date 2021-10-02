package com.example.googlemaplexample;

import androidx.appcompat.app.AppCompatActivity;


import android.app.FragmentManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geoCoder;
    private Button button;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);

        //
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        geoCoder = new Geocoder(this);

        // 맵 터치 이벤트
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions mOptions = new MarkerOptions();
                // 마커 제목
                mOptions.title("마커 좌표");
                Double latitude = point.latitude;
                Double longtitude = point.longitude;
                // 마커 텍스트 설정
                mOptions.snippet(latitude.toString() + "," + longtitude.toString());
                // LatLng : 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(latitude,longtitude));
                // 마커 추가
                googleMap.addMarker(mOptions);
            }
        });
        //////
        // 버튼 이벤트
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editText.getText().toString();
                List<Address> addressList = null;
                try {
                    addressList = geoCoder.getFromLocationName(str,10);
                } catch (IOException e){
                    e.printStackTrace();
                }

                System.out.println(addressList.get(0).toString());

                String []splitStr = addressList.get(0).toString().split(",");
                String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1
                        , splitStr[0].length() - 2);
                System.out.println(address);

                String latitude = splitStr[10].substring(splitStr[10].indexOf("=")+ 1);
                String longitude = splitStr[12].substring(splitStr[12].indexOf("=")+1);
                System.out.println(latitude);
                System.out.println(longitude);

                LatLng point = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));

                MarkerOptions mOptions2 = new MarkerOptions();
                mOptions2.title("search result");
                mOptions2.snippet(address);
                mOptions2.position(point);
                // 마커 추가
                mMap.addMarker(mOptions2);
                // 해당 좌표로 화면 줌
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));
            }
        });

        LatLng sydney = new LatLng(-34,151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }
}