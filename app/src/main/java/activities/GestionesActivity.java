package activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;

import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import adapters.AdapterGenerico;
import adapters.AdapterGestiones;
import app.hn.com.ficohsaseguros.R;
import dto.XmlContainer;
import interfaces.OnItemClickListener;
import models.XmlTokenLoginGestiones;
import models.XmlTokenLoginResult;
import util.XpathUtil;

/**
 * Created by mac on 1/11/15.
 */
public class GestionesActivity extends Activity {

    private RecyclerView mRecyclerView;
    private AdapterGestiones mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Activity activity;
    private XmlTokenLoginResult xmlTokenLoginResult ;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        activity = this;

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        getActionBar().setTitle("Gestiones");
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.actionbar_title);


        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String json = "";
        if (GetPrefs.contains(FicohsaConstants.JSON)) {
            json = GetPrefs.getString(FicohsaConstants.JSON, "");
            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new StringReader(json));
            xmlTokenLoginResult = gson.fromJson(br, XmlTokenLoginResult.class);
        }

        if(xmlTokenLoginResult.getXmlTokenLoginGestionesList() != null && xmlTokenLoginResult.getXmlTokenLoginGestionesList() .size() >0){
           setListData(xmlTokenLoginResult.getXmlTokenLoginGestionesList());
       }

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(Boolean.FALSE);
            }
        });

    }

    public void setListData(List<XmlTokenLoginGestiones> values) {
        // specify an adapter (see also next example)

        mAdapter = new AdapterGestiones(values);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = GetPrefs.edit();
                editor.putString(FicohsaConstants.GESTION_ID, xmlTokenLoginResult.getXmlTokenLoginGestionesList().get(position).getId_gestion());
                editor.commit();

                Intent ourintent = new Intent(getBaseContext(), RunMapActivity.class);
                //Bundle bundleGestion = new Bundle();
                //bundleGestion.putString("GESTION_ID", XmlContainer.xmlLocalizacionGestionList.get(position).getId_gestion());
                //ourintent.putExtras(bundleGestion);
                activity.startActivity(ourintent);

            }
        });

    }


}

