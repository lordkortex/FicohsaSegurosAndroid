package activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.w3c.dom.NodeList;

import adapters.AdapterEstadoCuenta;
import app.hn.com.ficohsaseguros.R;
import interfaces.OnItemClickListener;
import util.XpathUtil;

/**
 * Created by mac on 11/10/15.
 */
public class EstadoCuentaActivity extends Activity {

    private RecyclerView mRecyclerView;
    private AdapterEstadoCuenta mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        getActionBar().setTitle("Estado de Cuentas");

        final String xmlNotificacion1 = XpathUtil.buildXmlNotificacion("#Estado de Cuenta 100000", "Estado de Cuenta activo");

        final String cadena = "<NewDataSet>" +
                xmlNotificacion1 +
                xmlNotificacion1 +
                xmlNotificacion1 +
                "</NewDataSet>";


        NodeList nodeList = XpathUtil.getXptathResult(cadena, "/NewDataSet/notificacion");
        setListData(nodeList);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(Boolean.FALSE);
            }
        });

    }

    public void setListData(NodeList values) {
        // specify an adapter (see also next example)

        mAdapter = new AdapterEstadoCuenta(values);
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

