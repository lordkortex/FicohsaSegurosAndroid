package fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import activities.FicohsaConstants;
import adapters.AdapterDebitos;
import adapters.AdapterGenerico;
import adapters.AdapterNotificaciones;
import app.hn.com.ficohsaseguros.R;
import interfaces.OnItemClickListener;
import models.XmlNotificaciones;
import models.XmlTokenLoginResult;
import util.XpathUtil;

/**
 * Created by mac on 11/10/15.
 */
public class FragmentNotificaciones extends Fragment {

    private RecyclerView mRecyclerView;
    private AdapterNotificaciones mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private XmlTokenLoginResult xmlTokenLoginResult ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_notificaciones, container, false);

        return rootView;
    }

    /**
     * @param savedInstanceState which is the Bundle save instance.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        getActivity().getActionBar().setTitle("Debitos");

        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        String json = "";
        if (GetPrefs.contains(FicohsaConstants.JSON)) {
            json = GetPrefs.getString(FicohsaConstants.JSON, "");
            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new StringReader(json));
            xmlTokenLoginResult = gson.fromJson(br, XmlTokenLoginResult.class);
        }

        if(xmlTokenLoginResult.getXmlNotificaciones() != null && !xmlTokenLoginResult.getXmlNotificaciones().isEmpty()){
            setListData(xmlTokenLoginResult.getXmlNotificaciones());
        }

    }

    public void setListData(List<XmlNotificaciones> values) {
        // specify an adapter (see also next example)

        mAdapter = new AdapterNotificaciones(values);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {



            }
        });

    }


}

