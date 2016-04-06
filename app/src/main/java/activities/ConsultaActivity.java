package activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import java.util.ArrayList;

import adapters.AdapterMenuGridView;
import app.hn.com.ficohsaseguros.R;
import dto.MenuItem;

/**
 * Created by mac on 3/4/16.
 */
public class ConsultaActivity extends Activity {

    private ArrayList<MenuItem> gridArray = new ArrayList<MenuItem>();
    private AdapterMenuGridView customGridAdapter;
    private GridView gridView;
    private Activity activity;
    String password="";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_consultas_generales);
        //setContentView(R.layout.activity_grid_tipoasistencia);

        activity =this;

        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.actionbar_tittle_back);

        ImageButton b = (ImageButton) findViewById(R.id.imageViewBack);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });


        //*******************************************************************************************************
        // Cargando Menu Principal
        //*******************************************************************************************************
        Bitmap Poliza = BitmapFactory.decodeResource(this.getResources(), R.drawable.profile);
        Bitmap EstadoCuenta = BitmapFactory.decodeResource(this.getResources(), R.drawable.account_status);
        Bitmap EstadoSiniestro = BitmapFactory.decodeResource(this.getResources(), R.drawable.status);

        gridArray.add(new MenuItem(Poliza, "Póliza"));
        gridArray.add(new MenuItem(EstadoCuenta, "Estado de Cuenta"));
        gridArray.add(new MenuItem(EstadoSiniestro, "Estado de Trámites"));
        gridArray.add(new MenuItem(EstadoCuenta, "Débitos"));


        gridView = (GridView) findViewById(R.id.gridView1);
        customGridAdapter = new AdapterMenuGridView(this, R.layout.activity_grid_consultas_item, gridArray);
        gridView.setAdapter(customGridAdapter);

        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if (GetPrefs.contains(FicohsaConstants.PASSWORD)) {
            password = GetPrefs.getString(FicohsaConstants.PASSWORD, "");
        }



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                String tipoAsistencia = "";

                switch (position) {
                    case 0:
                        Intent ourintentNotify = new Intent(activity, PolizasActivity.class);
                        startActivity(ourintentNotify);

                        break;
                    case 1:
                        Intent ourintentEstadoCuenta = new Intent(activity, EstadoCuentaActivity.class);
                        startActivity(ourintentEstadoCuenta);
                        break;
                    case 2:
                        Intent ourintentEstadoSiniestro = new Intent(activity, EstadoSiniestroActivity.class);
                        startActivity(ourintentEstadoSiniestro);
                        break;
                    case 3:
                        Intent ourintentDebitos = new Intent(activity, DebitosActivity.class);
                        startActivity(ourintentDebitos);
                        break;

                }
                /*
                switch (position) {
                    case 0:
                        new ConsultaWebService(activity).execute(password + ";0");
                        break;
                    case 1:
                        new ConsultaWebService(activity).execute(password + ";1");
                        break;
                    case 2:
                        new ConsultaWebService(activity).execute(password + ";2");
                        break;
                    case 3:
                        new ConsultaWebService(activity).execute(password + ";3");
                        break;
                }*/

            }
        });


    }



}

