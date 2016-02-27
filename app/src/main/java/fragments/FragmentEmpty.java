package fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import activities.TipoAsistenciaActivity;
import app.hn.com.ficohsaseguros.R;
import asyntask.CrearGestionWebservice;
import dto.XmlContainer;

/**
 * Created by mac on 22/11/15.
 */
public class FragmentEmpty extends Fragment {

    private Activity activity;




    public FragmentEmpty() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.empty, container, false);

        return rootView;
    }


    /**
     * @param savedInstanceState which is the Bundle save instance.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();

    }
}
