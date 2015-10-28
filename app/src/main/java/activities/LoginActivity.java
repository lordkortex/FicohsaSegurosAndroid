package activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import app.hn.com.ficohsaseguros.R;
import intentservices.RSSPullService;

/**
 * Created by mac on 11/10/15.
 */
public class LoginActivity extends Activity {

    private Button loginButton;
    private EditText etUsuario;
    private Activity activity;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        activity = this;

        Intent startServiceIntent = new Intent(getBaseContext(), RSSPullService.class);
        PendingIntent startWebServicePendingIntent = PendingIntent.getService(getBaseContext(), 1111, startServiceIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000, startWebServicePendingIntent);




        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intentstop = new Intent(activity, RSSPullService.class);
                PendingIntent senderstop = PendingIntent.getService(activity, 1111, intentstop, 0);
                AlarmManager alarmManagerstop = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManagerstop.cancel(senderstop);

                Intent ourintent = new Intent(activity, MainActivity.class);
                startActivity(ourintent);
                finish();
            }
        });


    }
}
