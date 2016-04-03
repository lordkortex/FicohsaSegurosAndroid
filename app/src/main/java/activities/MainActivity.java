package activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

import app.hn.com.ficohsaseguros.DrawerItem;
import app.hn.com.ficohsaseguros.DrawerListAdapter;
import app.hn.com.ficohsaseguros.R;
import asyntask.ConsultaWebService;
import asyntask.ConsultarNotificaciones;
import asyntask.ObtenerCoordenadaWebService;
import dto.XmlContainer;
import fragments.FragmentConsulta;
import fragments.FragmentEmpty;
import fragments.FragmentMap;
import fragments.FragmentNotificaciones;
import fragments.FragmentPanicButton;
import fragments.FragmentTipoAsistencia;
import models.XmlTokenLoginResult;


public class MainActivity extends Activity implements LocationListener {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;

    private Activity activity;

    private String[] tagTitles;

    private XmlTokenLoginResult xmlTokenLoginResult ;

    private LocationManager locationManager;
    private String provider;

    private String latitud;
    private String longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        }


        activity = this;

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        ArrayList<DrawerItem> items = new ArrayList<DrawerItem>();


        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String json = "";
        if (GetPrefs.contains(FicohsaConstants.JSON)) {
            json = GetPrefs.getString(FicohsaConstants.JSON, "");
            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new StringReader(json));
            xmlTokenLoginResult = gson.fromJson(br, XmlTokenLoginResult.class);
        }


        if(xmlTokenLoginResult.getEsAsegurado().equalsIgnoreCase("true")){
            tagTitles = getResources().getStringArray(R.array.TagsCliente);
            items.add(new DrawerItem(tagTitles[0],R.drawable.home));
            items.add(new DrawerItem(tagTitles[1],R.drawable.location));
            items.add(new DrawerItem(tagTitles[2],R.drawable.notify));
            items.add(new DrawerItem(tagTitles[3],R.drawable.faq));
            items.add(new DrawerItem(tagTitles[4],R.drawable.exit));

        }else{
            tagTitles = getResources().getStringArray(R.array.TagsMotorista);
            items.add(new DrawerItem(tagTitles[0],R.drawable.home));
            items.add(new DrawerItem(tagTitles[1],R.drawable.location));
            items.add(new DrawerItem(tagTitles[2],R.drawable.notify));
            items.add(new DrawerItem(tagTitles[3],R.drawable.faq));
            items.add(new DrawerItem(tagTitles[4],R.drawable.exit));
        }




        mDrawerList.setAdapter(new DrawerListAdapter(this, items));
        mTitle = mDrawerTitle = getTitle();
        //mPlanetTitles = getResources().getStringArray(R.array.Tags);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        /*switch(item.getItemId()) {
            case R.id.action_websearch:
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }*/

        return super.onOptionsItemSelected(item);
    }


    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
           /* Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);*/

        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragmentPanicButton = new FragmentPanicButton();
        Bundle args = new Bundle();
        args.putInt("", position);
        args.putString("latitud", latitud);
        args.putString("longitud", longitud);
        fragmentPanicButton.setArguments(args);

        Fragment fragmentTipoAsistencia = new FragmentTipoAsistencia();
        args.putInt("", position);
        fragmentTipoAsistencia.setArguments(args);

        Fragment fragmentMap = new FragmentMap();
        args.putInt("", position);
        fragmentMap.setArguments(args);

        Fragment fragmentConsulta = new FragmentConsulta();
        fragmentConsulta.setArguments(args);

        FragmentEmpty fragmentEmpty = new FragmentEmpty();


        Fragment fragmentNotificaciones = new FragmentNotificaciones();
        fragmentNotificaciones.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();

        final String opcion = tagTitles[position];

        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String password="";
        if (GetPrefs.contains(FicohsaConstants.PASSWORD)) {
            password = GetPrefs.getString(FicohsaConstants.PASSWORD, "");
        }

        switch (opcion){
            case "Bienvenida":
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragmentEmpty).commit();
                break;
            case "Solicitar Asistencia":
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragmentPanicButton).commit();
                break;
            case "Ubicacion Asistencia":
                //Intent ourintent = new Intent(activity, MapActivity.class);
                //startActivity(ourintent);
                new ObtenerCoordenadaWebService(this).execute(password);

                break;
            case "Notificaciones":
                //Intent ourintentNotify = new Intent(activity, NotificacionesActivity.class);
                //startActivity(ourintentNotify);
                new ConsultarNotificaciones(this).execute(password);

                break;
            case "Consultas Generales":
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragmentConsulta).commit();
                break;
            case "Gestiones":
                Intent ourintenvGestiones = new Intent(activity, GestionesActivity.class);
                startActivity(ourintenvGestiones);
                //finish();
                break;
            case "Cerrar Sesion":
                //SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = GetPrefs.edit();
                editor.putString(FicohsaConstants.IS_LOGGED, "FALSE");
                editor.commit();

                Intent ourintenvLogin = new Intent(activity, LoginActivity.class);
                startActivity(ourintenvLogin);
                finish();
                break;

        }


        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        //setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = (location.getLatitude());
        double lng =  (location.getLongitude());
        latitud = String.valueOf(lat);
        longitud= String.valueOf(lng);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }


}