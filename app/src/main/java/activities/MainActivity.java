package activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
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

import java.util.ArrayList;

import app.hn.com.ficohsaseguros.DrawerItem;
import app.hn.com.ficohsaseguros.DrawerListAdapter;
import app.hn.com.ficohsaseguros.R;
import fragments.FragmentConsulta;
import fragments.FragmentMap;
import fragments.FragmentNotificaciones;
import fragments.FragmentPanicButton;
import fragments.FragmentTipoAsistencia;


public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);

        activity = this;

        String[] tagTitles = getResources().getStringArray(R.array.Tags);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        ArrayList<DrawerItem> items = new ArrayList<DrawerItem>();
        items.add(new DrawerItem(tagTitles[0],R.drawable.ic_action_wrench));
        items.add(new DrawerItem(tagTitles[1],R.drawable.ic_action_world));
        items.add(new DrawerItem(tagTitles[2],R.drawable.ic_action_file));
        items.add(new DrawerItem(tagTitles[3],R.drawable.ic_action_calendar));
        items.add(new DrawerItem(tagTitles[4],R.drawable.ic_action));

        mDrawerList.setAdapter(new DrawerListAdapter(this, items));
        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.Tags);
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
        fragmentPanicButton.setArguments(args);

        Fragment fragmentTipoAsistencia = new FragmentTipoAsistencia();
        args.putInt("", position);
        fragmentTipoAsistencia.setArguments(args);

        Fragment fragmentMap = new FragmentMap();
        args.putInt("", position);
        fragmentMap.setArguments(args);

        Fragment fragmentConsulta = new FragmentConsulta();
        fragmentConsulta.setArguments(args);

        Fragment fragmentNotificaciones = new FragmentNotificaciones();
        fragmentNotificaciones.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        switch (position){
            case 0:
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragmentPanicButton).commit();
                break;
            case 1:
                Intent ourintent = new Intent(activity, MapActivity.class);
                startActivity(ourintent);
                break;
            case 2:
                Intent ourintentNotify = new Intent(activity, NotificacionesActivity.class);
                startActivity(ourintentNotify);
                //fragmentManager.beginTransaction().replace(R.id.conten
                // t_frame, fragmentNotificaciones).commit();
                break;
            case 3:
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragmentConsulta).commit();
                break;
            case 4:
                Intent ourintenvLogin = new Intent(activity, LoginActivity.class);
                startActivity(ourintenvLogin);
                finish();
                break;
        }


        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
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


}