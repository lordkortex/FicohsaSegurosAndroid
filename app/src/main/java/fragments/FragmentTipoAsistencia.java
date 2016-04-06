package fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

import activities.FicohsaConstants;
import adapters.AdapterMenuGridView;
import app.hn.com.ficohsaseguros.R;
import asyntask.CrearAsistenciaWebservice;
import dto.MenuItem;
import models.XmlTokenLoginResult;

/**
 * Created by mac on 7/10/15.
 */
public class FragmentTipoAsistencia extends Fragment {

    private ArrayList<MenuItem> gridArray = new ArrayList<MenuItem>();
    private AdapterMenuGridView customGridAdapter;
    private GridView gridView;
    private Activity activity;
    private String tipoAsistencia = "";
    private String password;
    private XmlTokenLoginResult xmlTokenLoginResult ;

    public FragmentTipoAsistencia() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_grid_tipoasistencia, container, false);

        return rootView;
    }


    /**
     * @param savedInstanceState which is the Bundle save instance.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();

        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        if (GetPrefs.contains(FicohsaConstants.PASSWORD)) {
            password = GetPrefs.getString(FicohsaConstants.PASSWORD, "");
        }

        //*******************************************************************************************************
        // Cargando Menu Principal
        //*******************************************************************************************************
        Bitmap Ambulancia = BitmapFactory.decodeResource(this.getResources(), R.drawable.ambulance);
        Bitmap Grua = BitmapFactory.decodeResource(this.getResources(), R.drawable.crane);
        Bitmap Gasolina = BitmapFactory.decodeResource(this.getResources(), R.drawable.gasoline);
        Bitmap Bateria = BitmapFactory.decodeResource(this.getResources(), R.drawable.batery);
        Bitmap Llantas = BitmapFactory.decodeResource(this.getResources(), R.drawable.tires);
        Bitmap Otros = BitmapFactory.decodeResource(this.getResources(), R.drawable.keymaster);

        gridArray.add(new MenuItem(Ambulancia, "Ambulancia"));
        gridArray.add(new MenuItem(Grua, "Grua"));
        gridArray.add(new MenuItem(Gasolina, "Gasolina"));

        gridArray.add(new MenuItem(Bateria, "Paso de Corriente"));
        gridArray.add(new MenuItem(Llantas, "Cambio de Llanta"));
        gridArray.add(new MenuItem(Otros, "Cerrajería Vial"));

        gridView = (GridView) getActivity().findViewById(R.id.gridView1);
        customGridAdapter = new AdapterMenuGridView(getActivity(), R.layout.activity_tipoasistencia_item, gridArray);
        gridView.setAdapter(customGridAdapter);



        //*******************************************************************************************************
        // Obteniendo Json desde las preferencias
        //*******************************************************************************************************
        String json = "";
        if (GetPrefs.contains(FicohsaConstants.JSON)) {
            json = GetPrefs.getString(FicohsaConstants.JSON, "");
            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new StringReader(json));
            xmlTokenLoginResult = gson.fromJson(br, XmlTokenLoginResult.class);
        }


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {



                switch (position) {
                    case 0:
                        tipoAsistencia = "1002001";
                        break;
                    case 1:
                        tipoAsistencia = "1002002";
                        break;
                    case 2:
                        tipoAsistencia = "1002003";
                        break;
                    case 3:
                        tipoAsistencia = "1002004";
                        break;
                    case 4:
                        tipoAsistencia = "1002005";
                        break;
                    case 5:
                        tipoAsistencia = "1002006";
                        break;

              }

                String latitud = getArguments().getString("latitud");
                String longitud = getArguments().getString("longitud");

                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse("tel:" + xmlTokenLoginResult.getTxtTelefonoAsistencia()));
                startActivity(phoneIntent);

                new CrearAsistenciaWebservice(activity).execute(password + ";" + tipoAsistencia + ";"+ latitud +";" + longitud);

                /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());
                alertDialogBuilder.setTitle("Solicitud de Asistencia");
                alertDialogBuilder
                        .setMessage("Solicitud enviada exitosamente. Ahora intentare contactar a Asistencia Ficohsa Seguros ?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                                phoneIntent.setData(Uri.parse("tel:" + XmlContainer.xmlTokenLoginResult.getTxtTelefonoAsistencia()));
                                startActivity(phoneIntent);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                new CrearGestionWebservice(activity).execute(password + ";"+ tipoAsistencia +";0;0;android;ios");

                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();*/

            }
        });


    }

}
