package activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import adapters.AdapterDebitos;
import app.hn.com.ficohsaseguros.R;
import interfaces.OnItemClickListener;
import models.XmlDebitos;
import models.XmlEstadoCuenta;
import models.XmlTokenLoginResult;

/**
 * Created by mac on 28/3/16.
 */
public class DebitosActivity extends Activity {

    private RecyclerView mRecyclerView;
    private AdapterDebitos mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private XmlTokenLoginResult xmlTokenLoginResult ;
    private Activity activity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        activity = this;

        //getActionBar().setTitle("Ficohsa | Seguros");
        //getActionBar().setTitle("Debitos");
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

        if(xmlTokenLoginResult.getXmlDebitos() != null && !xmlTokenLoginResult.getXmlDebitos().isEmpty()){
            setListData(xmlTokenLoginResult.getXmlDebitos());
        }

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(Boolean.FALSE);
            }
        });

    }

    public void setListData(List<XmlDebitos> values) {
        // specify an adapter (see also next example)

        mAdapter = new AdapterDebitos(values);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

               /* final String generic_field_1 = ((TextView) view.findViewById(R.id.generic_field_1)).getText().toString();
                final String generic_field_2 = ((TextView) view.findViewById(R.id.generic_field_2)).getText().toString();
                final String generic_field_3 = ((TextView) view.findViewById(R.id.generic_field_3)).getText().toString();
                final String generic_field_4 = ((TextView) view.findViewById(R.id.generic_field_4)).getText().toString();
                final String generic_field_5 = ((TextView) view.findViewById(R.id.generic_field_5)).getText().toString();

                Bundle bundleProyecto = new Bundle();
                bundleProyecto.putString("FIELD_PARTIDO_NOMBRE", generic_field_1);
                bundleProyecto.putString("FIELD_PARTIDO_DESC", generic_field_2);
                bundleProyecto.putString("FIELD_DESC_CORTA", generic_field_3);
                bundleProyecto.putString("FIELD_DESC_LARGA", generic_field_4);
                bundleProyecto.putString("FIELD_IMAGEN", generic_field_5);*/


            }
        });

    }


}
