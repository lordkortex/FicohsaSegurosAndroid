package activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import app.hn.com.ficohsaseguros.R;

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

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                            Intent ourintent = new Intent(activity, MainActivity.class);
                            startActivity(ourintent);
                finish();

        }});


    }
}
