package edu.illinois.cs465.accessimap;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.ui.AnimationUtil;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import edu.illinois.cs465.accessimap.databinding.ActivityOutdoorNavigationBinding;

public class OutdoorNavActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private ActivityOutdoorNavigationBinding binding;

    private Marker currentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOutdoorNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng cif = new LatLng(40.11248375062642, -88.22834920290913);
        currentMarker = mMap.addMarker(new MarkerOptions().position(cif).title("CIF").snippet("CIF building").icon(BitmapFromVector(
                getApplicationContext(),
                R.drawable.person)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cif, 16));

        LatLng siebel = new LatLng(40.10296632335999, -88.2327589803859);
//        mMap.addMarker(new MarkerOptions().position(siebel).title("Siebel"));

        // TODO: load from route json depending on start and end buildings
//        String route = loadJSONFromAsset();

        List<LatLng> decodedPath = PolyUtil.decode("glysFbboyO?lBNAd@CnAC`FAX??FtB?nAAh@@`B@rHIRLDHLf@@hKl@A@^@pADHZ??n@`@?z@AT?tB??bB@x@@FLPPD|@?VDv@p@DDBHtD?|@A");
        Log.d("decodedPath", decodedPath.toString());
        mMap.addPolyline(new PolylineOptions().addAll(decodedPath));

//        for (int i = 0; i < decodedPath.size(); i++) {
//            AnimationUtil.animateMarkerTo(currentMarker, decodedPath.get(i));
//            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(decodedPath.get(i), 16);
//            mMap.animateCamera(cameraUpdate);
//        }
        final Handler handler = new Handler(Looper.getMainLooper());
        for (int i = 0; i < decodedPath.size(); i++) {
            int finalI = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    currentMarker.setPosition(decodedPath.get(finalI));
                }
            }, 3000);
//            AnimationUtil.animateMarkerTo(currentMarker, decodedPath.get(i));
//            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(decodedPath.get(i), 16);
//            mMap.animateCamera(cameraUpdate);
        }


    }

    // https://www.geeksforgeeks.org/how-to-add-custom-marker-to-google-maps-in-android/
    private BitmapDescriptor
    BitmapFromVector(Context context, int vectorResId)
    {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(
                context, vectorResId);

        // below line is use to set bounds to our vector
        // drawable.
        vectorDrawable.setBounds(
                0, 0, vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(
                vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our
        // bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}