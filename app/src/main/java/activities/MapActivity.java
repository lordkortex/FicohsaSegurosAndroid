package activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import app.hn.com.ficohsaseguros.R;
import asyntask.LoginWebService;
import asyntask.ObtenerCoordenadaWebService;
import dto.XmlContainer;
import models.XmlLocalizacionGestion;
import models.XmlLocalizacionPuntos;
import models.XmlLocalizacionToken;
import models.XmlTokenLoginResult;

/**
 * Created by mac on 11/10/15.
 */
public class MapActivity extends Activity {
    //static final LatLng FicohsaPoint = new LatLng(40.76793169992044, -73.98180484771729);
    private GoogleMap googleMap;
    private Context context;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //getActionBar().setTitle("Ubicacion");

        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.actionbar_tittle_back);

        ImageButton b = (ImageButton) findViewById(R.id.imageViewBack);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });


        activity = this;


       // new ObtenerCoordenadaWebService(this).execute("abcd72015");

        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }



            double latitudDestino = Double.parseDouble(XmlContainer.xmlLocalizacionGestion.getLatitudDestino()) ;
            double longitudDestino = Double.parseDouble(XmlContainer.xmlLocalizacionGestion.getLongitudDestino());

            final LatLng FicohsaPoint = new LatLng(latitudDestino, longitudDestino);

            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            Marker TP = googleMap.addMarker(new MarkerOptions().
                    position(FicohsaPoint).title("Ficohsa Seguros"));

            CameraUpdate center =
                    CameraUpdateFactory.newLatLng(FicohsaPoint);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);


            List<XmlLocalizacionToken> xmlLocalizacionTokenList = XmlContainer.xmlLocalizacionGestion.getXmlLocalizacionTokenList();

            int initPoint = 0;
            for(XmlLocalizacionToken itemlocToken : xmlLocalizacionTokenList){

                PolylineOptions ficohsaPoints = new PolylineOptions();
                List<XmlLocalizacionPuntos> xmlLocalizacionPuntosList = itemlocToken.getXmlLocalizacionPuntosList();
                double latitud = 0;
                double longitud = 0;
                for ( XmlLocalizacionPuntos item :  xmlLocalizacionPuntosList ){
                    latitud = Double.parseDouble(item.getLatitud());
                    longitud = Double.parseDouble(item.getLongitud());
                    LatLng pointGestion = new LatLng(latitud, longitud);
                    ficohsaPoints.add(pointGestion);

                    if(initPoint == 0){
                        googleMap.addMarker(new MarkerOptions().position(pointGestion).title("Punto Inicial"));
                        initPoint ++;
                    }


                }
                initPoint = 0;
                LatLng pointGestion = new LatLng(latitud, longitud);
                googleMap.addMarker(new MarkerOptions().position(pointGestion).title("Punto Final"));

                Polyline polyline = googleMap.addPolyline(ficohsaPoints);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}