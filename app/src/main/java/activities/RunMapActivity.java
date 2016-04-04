package activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.hn.com.ficohsaseguros.R;
import asyntask.LoginWebService;
import dto.XmlContainer;
import intentservices.RSSPullService;
import models.XmlMotivos;
import models.XmlTokenLoginResult;

/**
 * Created by mac on 1/11/15.
 */
public class RunMapActivity extends Activity {

    private Button comenzarButton,detenerButton;
    private Activity activity;
    private AlertDialog.Builder dialogBuilder;
    private XmlTokenLoginResult xmlTokenLoginResult ;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_run);
        activity = this;

        //getActionBar().setTitle("Ficohsa | Seguros");
        //getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getActionBar().setCustomView(R.layout.actionbar_title);

        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.actionbar_tittle_back);

        ImageButton b = (ImageButton) findViewById(R.id.imageViewBack);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });



        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String json = "";
        if (GetPrefs.contains(FicohsaConstants.JSON)) {
            json = GetPrefs.getString(FicohsaConstants.JSON, "");
            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new StringReader(json));
            xmlTokenLoginResult = gson.fromJson(br, XmlTokenLoginResult.class);
        }

        final ArrayList<String> listMotivos = new ArrayList<String>();

        Map<String,String> xmlMotivosMap = new HashMap<String,String>();
        for(XmlMotivos xmlMotivos : xmlTokenLoginResult.getXmlMotivosList()){
            if(!xmlMotivosMap.containsKey(xmlMotivos.getId_motivo())){
                xmlMotivosMap.put(xmlMotivos.getId_motivo(),xmlMotivos.getTxt_motivo());
                listMotivos.add(xmlMotivos.getTxt_motivo());
            }
        }


        CharSequence[] items = listMotivos.toArray(new CharSequence[listMotivos.size()]);

        dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder.setTitle("Pick an option");
        dialogBuilder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do anything you want here
            }

        });

        comenzarButton = (Button) findViewById(R.id.buttonComenzar);
        detenerButton = (Button) findViewById(R.id.buttonDetener);


        if (GetPrefs.contains(FicohsaConstants.RUN_MAP_STARTED) ) {
            final String isRunMapStarted = GetPrefs.getString(FicohsaConstants.RUN_MAP_STARTED, "");
            if(isRunMapStarted.equals("TRUE")){
                comenzarButton.setEnabled(false);
                comenzarButton.setVisibility(View.INVISIBLE);
                detenerButton.setEnabled(true);
                detenerButton.setVisibility(View.VISIBLE);
            }else{
                comenzarButton.setEnabled(true);
                comenzarButton.setVisibility(View.VISIBLE);
                detenerButton.setEnabled(false);
                detenerButton.setVisibility(View.INVISIBLE);
            }
        } else {
            comenzarButton.setVisibility(View.VISIBLE);
            comenzarButton.setEnabled(true);
            detenerButton.setVisibility(View.INVISIBLE);
            detenerButton.setEnabled(false);
        }





        comenzarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
                SharedPreferences.Editor editor = GetPrefs.edit();
                editor.putString(FicohsaConstants.RUN_MAP_STARTED, "FALSE");
                editor.commit();

                comenzarButton.setVisibility(View.INVISIBLE);
                comenzarButton.setEnabled(false);
                detenerButton.setVisibility(View.VISIBLE);
                detenerButton.setEnabled(true);


                Intent startServiceIntent = new Intent(getBaseContext(), RSSPullService.class);
                PendingIntent startWebServicePendingIntent = PendingIntent.getService(getBaseContext(), 1111, startServiceIntent, 0);
                AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000, startWebServicePendingIntent);
                Toast.makeText(activity, "Servicio iniciado", Toast.LENGTH_LONG).show();
                ;
            }
        });


        detenerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
                SharedPreferences.Editor editor = GetPrefs.edit();
                editor.putString(FicohsaConstants.RUN_MAP_STARTED, "TRUE");
                editor.commit();

                comenzarButton.setVisibility(View.VISIBLE);
                comenzarButton.setEnabled(true);
                detenerButton.setVisibility(View.INVISIBLE);
                detenerButton.setEnabled(false);



                dialogBuilder.create().show();
                Intent intentstop = new Intent(activity, RSSPullService.class);
                PendingIntent senderstop = PendingIntent.getService(activity, 1111, intentstop, 0);
                AlarmManager alarmManagerstop = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManagerstop.cancel(senderstop);
                Toast.makeText(activity, "Servicio Detenido", Toast.LENGTH_LONG).show();
            }
        });

    }
}
