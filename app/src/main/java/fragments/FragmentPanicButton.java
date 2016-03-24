package fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.StringReader;

import activities.FicohsaConstants;
import activities.TipoAsistenciaActivity;
import app.hn.com.ficohsaseguros.R;
import asyntask.CrearGestionWebservice;
import models.XmlTokenLoginResult;

/**
 * Created by mac on 7/10/15.
 */
public class FragmentPanicButton extends Fragment {

    private Button loginButton;
    private ImageButton panicButton;
    private Activity activity;
    private XmlTokenLoginResult xmlTokenLoginResult ;
    private String password,latitud,longitud;




    public FragmentPanicButton() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_panic, container, false);

        latitud = getArguments().getString("latitud");
        longitud = getArguments().getString("longitud");

        return rootView;
    }


    /**
     * @param savedInstanceState which is the Bundle save instance.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();

        loginButton = (Button) activity.findViewById(R.id.loginButton);
        panicButton = (ImageButton) activity.findViewById(R.id.imageButton);

        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        String json = "";
        if (GetPrefs.contains(FicohsaConstants.JSON)) {
            json = GetPrefs.getString(FicohsaConstants.JSON, "");
            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new StringReader(json));
            xmlTokenLoginResult = gson.fromJson(br, XmlTokenLoginResult.class);
        }

        if (GetPrefs.contains(FicohsaConstants.PASSWORD)) {
            password = GetPrefs.getString(FicohsaConstants.PASSWORD, "");
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent ourintenConsultas = new Intent(activity, TipoAsistenciaActivity.class);
                startActivity(ourintenConsultas);

     /*           AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());
                alertDialogBuilder.setTitle("Error de Comunicacion");
                alertDialogBuilder
                        .setMessage("Imposible comunicarse con asistencia Ficohsa. Levantar Llamada ?")
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
                                //dialog.cancel();
                                Intent ourintenConsultas = new Intent(activity, TipoAsistenciaActivity.class);
                                startActivity(ourintenConsultas);
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();*/

            }
        });

        panicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tokenAndroid = "....";
                SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
                if (GetPrefs.contains(FicohsaConstants.TOKEN_ANDROID)) {
                    tokenAndroid = GetPrefs.getString(FicohsaConstants.TOKEN_ANDROID, "");
                }

                latitud = getArguments().getString("latitud");
                longitud = getArguments().getString("longitud");

                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse("tel:" + xmlTokenLoginResult.getTxtTelefonoAsistencia()));
                startActivity(phoneIntent);
                new CrearGestionWebservice(activity).execute(password +";" + latitud + ";"+ longitud + ";" + tokenAndroid);


                /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());
                alertDialogBuilder.setTitle("Error de Comunicacion");
                alertDialogBuilder
                        .setMessage("Imposible comunicarse con asistencia Ficohsa. Levantar Llamada ?")
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
                                //dialog.cancel();
                                new CrearGestionWebservice(activity).execute("abcd72015;0;0;0;android;ios");
                                //Intent ourintenConsultas = new Intent(activity, TipoAsistenciaActivity.class);
                                //startActivity(ourintenConsultas);
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();*/

            }
        });

    }

}
