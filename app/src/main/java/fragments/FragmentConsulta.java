package fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import adapters.AdapterMenuGridView;
import app.hn.com.ficohsaseguros.R;
import dto.MenuItem;

/**
 * Created by mac on 11/10/15.
 */
public class FragmentConsulta  extends Fragment {

    private ArrayList<MenuItem> gridArray = new ArrayList<MenuItem>();
    private AdapterMenuGridView customGridAdapter;
    private GridView gridView;
    private Activity activity;

    public FragmentConsulta() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tipoasistencia, container, false);

        return rootView;
    }


    /**
     * @param savedInstanceState which is the Bundle save instance.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
        //*******************************************************************************************************
        // Cargando Menu Principal
        //*******************************************************************************************************
        Bitmap Poliza = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_action_as_e);
        Bitmap EstadoCuenta = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_action_as_d);
        Bitmap EstadoSiniestro = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_action_as_f);

        gridArray.add(new MenuItem(Poliza, "Poliza"));
        gridArray.add(new MenuItem(EstadoCuenta, "Estado de Cuenta"));
        gridArray.add(new MenuItem(EstadoSiniestro, "Estado de Siniestros"));


        gridView = (GridView) getActivity().findViewById(R.id.gridView1);
        customGridAdapter = new AdapterMenuGridView(getActivity(), R.layout.tipoasistenciaitem, gridArray);
        gridView.setAdapter(customGridAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                String tipoAsistencia = "";

                switch (position) {
                    case 0:
                        tipoAsistencia = "";
                        break;
                    case 1:
                        tipoAsistencia = "";
                        break;
                    case 2:
                        tipoAsistencia = "";
                        break;
                    case 3:
                        tipoAsistencia = "";
                        break;
                    case 4:
                        tipoAsistencia = "";
                        break;
                    case 5:
                        tipoAsistencia = "";
                        break;

                }

                /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());
                alertDialogBuilder.setTitle("Solicitud de Asistencia");
                alertDialogBuilder
                        .setMessage("Solicitud enviada exitosamente. Ahora intentare contactar a Asistencia Ficohsa Seguros ?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                                phoneIntent.setData(Uri.parse("tel:31561390"));
                                startActivity(phoneIntent);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();*/

            }
        });


    }

}