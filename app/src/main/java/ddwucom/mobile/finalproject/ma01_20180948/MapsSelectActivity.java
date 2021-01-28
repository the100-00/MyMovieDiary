package ddwucom.mobile.finalproject.ma01_20180948;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class MapsSelectActivity extends AppCompatActivity implements OnMapReadyCallback {

    final static String TAG = "MainActivity";
    final static int PERMISSION_REQ_CODE = 100;

    private GoogleMap mGoogleMap;
    private LocationManager locManager;
    private LatLng currentLoc;
    private MarkerOptions markerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_maps);

        locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        mapLoad();
        locationUpdate();
    }

    private void locationUpdate(){
        if(checkPermission()){
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,0,locationListener);
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLoc = new LatLng(location.getLatitude(),location.getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc,15));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {        }

        @Override
        public void onProviderEnabled(String provider) {        }

        @Override
        public void onProviderDisabled(String provider) {        }
    };


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_return:
                finish();
                break;
        }
    }

    PlacesListener placesListener = new PlacesListener() {

        @Override
        public void onPlacesFailure(PlacesException e) {        }

        @Override
        public void onPlacesStart() {        }

        @Override
        public void onPlacesSuccess(final List<noman.googleplaces.Place> places) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (noman.googleplaces.Place place : places){

                        LatLng latLng = new LatLng(place.getLatitude(),place.getLongitude());
                        String address = getCurrentAddress(latLng);
                        markerOptions.title(place.getName());
                        markerOptions.snippet(address);
                        markerOptions.position(new LatLng(place.getLatitude(),place.getLongitude()));
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_RED
                        ));
                        Marker newMarker = mGoogleMap.addMarker(markerOptions);
                        newMarker.setTag(place.getPlaceId());
                    }
                    mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK,resultIntent);
                            resultIntent.putExtra("title", marker.getTitle());
                            finish();
                        }
                    });
                }
            });
        }


        @Override
        public void onPlacesFinished() {        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        markerOptions = new MarkerOptions();

        final LatLng current = new LatLng(36.99, 127.92); //충주시, 한국 전체
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current,7));

        if(checkPermission())
            mGoogleMap.setMyLocationEnabled(true);


        mGoogleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                currentLoc = new LatLng(location.getLatitude(),location.getLongitude());
                Toast.makeText(MapsSelectActivity.this,
                        "주변 영화관 검색을 시작합니다.",Toast.LENGTH_SHORT).show();
                searchStart(currentLoc);
            }
        });

        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                return false;
            }
        });

    }

    public void searchStart(LatLng location)
    {
        Log.d(TAG,location.latitude+","+location.longitude);
        new NRPlaces.Builder().listener(placesListener)
                .key("AIzaSyDPrirqtMMwnFPEjPkpWnWNV_MNWmoGSt0")
                .latlng(location.latitude,location.longitude)
                .radius(2000)
                .type(PlaceType.MOVIE_THEATER)
                .build()
                .execute();
    }


    /*구글맵을 멤버변수로 로딩*/
    private void mapLoad() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);// 매배변수 this: MainActivity 가 OnMapReadyCallback 을 구현하므로
    }


    /* 필요 permission 요청 */
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQ_CODE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 퍼미션을 획득하였을 경우 맵 로딩 실행
                mapLoad();
                locationUpdate();
            } else {
                // 퍼미션 미획득 시 액티비티 종료
                Toast.makeText(this, "앱 실행을 위해 권한 허용이 필요함", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    public String getCurrentAddress(LatLng latlng) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }

}
