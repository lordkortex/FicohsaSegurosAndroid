package activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import app.hn.com.ficohsaseguros.R;
import asyntask.LoginWebService;

/**
 * Created by mac on 11/10/15.
 */
public class LoginActivity extends Activity {

    private Button loginButton;
    private EditText etPassword;
    public static Activity activity;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activity = this;

        etPassword = (EditText) findViewById(R.id.tvPassword);


        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (GetPrefs.contains(FicohsaConstants.IS_LOGGED) ) {
            final String isLogged = GetPrefs.getString(FicohsaConstants.IS_LOGGED, "");
            if(isLogged.equals("TRUE")){
                Intent ourintent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(ourintent);
                LoginActivity.activity.finish();
            }
        }

        /*
        Intent startServiceIntent = new Intent(getBaseContext(), RSSPullService.class);
        PendingIntent startWebServicePendingIntent = PendingIntent.getService(getBaseContext(), 1111, startServiceIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000, startWebServicePendingIntent);
        */



        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                final String password = etPassword.getText().toString();
                SharedPreferences.Editor editor = GetPrefs.edit();
                editor.putString(FicohsaConstants.PASSWORD, password);
                editor.commit();

                if(password.equals("")){
                    Toast.makeText(activity, "El token no debe estar vacio.", Toast.LENGTH_LONG).show();
                }else{
                    new LoginWebService(activity).execute(password + ";csfsdfd-dhdhd-djdhdd");

                }




                //new CoordenadaWebService(activity).execute("abcd72015");

                //new CrearGestionWebservice(activity).execute("abcd72015;0;0;0;android;ios");

                /*
                Intent intentstop = new Intent(activity, RSSPullService.class);
                PendingIntent senderstop = PendingIntent.getService(activity, 1111, intentstop, 0);
                AlarmManager alarmManagerstop = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManagerstop.cancel(senderstop);
                */


            }
        });


    }
}
