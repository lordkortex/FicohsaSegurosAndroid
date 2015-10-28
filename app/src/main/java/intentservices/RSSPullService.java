package intentservices;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;

import asyntask.CoordinateDataWebService;

/**
 * Created by mac on 26/10/15.
 */
public class RSSPullService extends Service  {


    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

    private static final long MIN_TIME_BW_UPDATES = 1000 * 10 * 1; // 1 minute

    private final Context mContext = null;

    Service thisService;

    public RSSPullService() {
    }

    String tag="TestService";
    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Service created...", Toast.LENGTH_LONG).show();
        Log.i(tag, "Service created...");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStart(intent, startId);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Intent Myintent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS) ;
            Myintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Myintent);
            Toast.makeText(this, "El GPS esta desactivado en tu dispositivo", Toast.LENGTH_SHORT).show();
        }

        //*******************************************************************************************************
        // Busca las coordenadas del GPS
        //*******************************************************************************************************
        double latitudeGps = 0.0;
        double longitudeGps = 0.0;

        GPSTracker gps;
        gps = new GPSTracker(RSSPullService.this);

        if (gps != null) {


            if (gps.canGetLocation()) {
                latitudeGps = gps.getLatitude();
                longitudeGps = gps.getLongitude();
            }
        }

        final String coordinates = latitudeGps + ";" + longitudeGps;
        new CoordinateDataWebService(this).execute(coordinates);
        Log.i(tag, "Service started...");
        return 1;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service destroyed...", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }


}