package activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import app.hn.com.ficohsaseguros.R;
import fragments.FragmentTipoAsistencia;

/**
 * Created by mac on 11/10/15.
 */
public class TipoAsistenciaActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setTitle("Tipo Asistencia");

        Bundle bundle = getIntent().getExtras();
        String latitud = bundle.getString("latitud");
        String longitud = bundle.getString("longitud");

        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragmentTipoAsistencia = new FragmentTipoAsistencia();

        Bundle args = new Bundle();
        args.putString("latitud", latitud);
        args.putString("longitud", longitud);
        fragmentTipoAsistencia.setArguments(args);

        fragmentManager.beginTransaction().replace(R.id.content_frame, fragmentTipoAsistencia).commit();




    }
}