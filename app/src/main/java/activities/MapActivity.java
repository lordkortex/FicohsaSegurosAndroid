package activities;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import app.hn.com.ficohsaseguros.R;

/**
 * Created by mac on 11/10/15.
 */
public class MapActivity extends Activity {
    static final LatLng FicohsaPoint = new LatLng(40.76793169992044 , -73.98180484771729);
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getActionBar().setTitle("Ubicacion");


        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            Marker TP = googleMap.addMarker(new MarkerOptions().
                    position(FicohsaPoint).title("Ficohsa Seguros"));

            CameraUpdate center=
                    CameraUpdateFactory.newLatLng(FicohsaPoint);
            CameraUpdate zoom= CameraUpdateFactory.zoomTo(15);

            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}