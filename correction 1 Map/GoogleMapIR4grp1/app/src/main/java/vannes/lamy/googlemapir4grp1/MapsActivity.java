package vannes.lamy.googlemapir4grp1;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import vannes.lamy.googlemapir4grp1.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
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

        LatLng esaip= new LatLng(47.46425, -0.49737);
        mMap.addMarker(new MarkerOptions().position(esaip).title("ESAIP Angers"));
        //zoom en 15x
        CameraPosition cameraPos= new CameraPosition.Builder().target(esaip).zoom(15).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
        //type de carte
        // mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN;
        // mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL;
        //affichage bousole
        mMap.getUiSettings().setCompassEnabled(true);
        //activation de la rotation
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        //activation des controles du zoom
        mMap.getUiSettings().setZoomControlsEnabled(true) ;
        //insertion marker sur le chateau d'Angers
        LatLng castleAngers= new LatLng(47.47020, -0.56008);
        //definition du titre et de la couleur du marker
        MarkerOptions chatAngers=new MarkerOptions().position(castleAngers).title("Chateau d'Angers");
        chatAngers.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        //ajout du marker sur la carte
        mMap.addMarker(chatAngers);
        //listener sur le marker pour lancer Google streetView
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng coordo= marker.getPosition();
                Double lat=coordo.latitude;
                Double longi= coordo.longitude;
                String uri = "google.streetview:cbll="+lat+","+longi+"&cbp=0,30,0,0,-15";
                Intent streetView=new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                streetView.setPackage("com.google.android.apps.maps");
                (MapsActivity.this).startActivity(streetView);
                return false;
            }
        });
    }
}