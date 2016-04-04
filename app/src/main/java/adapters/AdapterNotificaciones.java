package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.hn.com.ficohsaseguros.R;
import interfaces.OnItemClickListener;
import models.XmlNotificaciones;

/**
 * Created by mac on 29/3/16.
 */
public class AdapterNotificaciones extends RecyclerView.Adapter<AdapterNotificaciones.ViewHolder> {

    private List<XmlNotificaciones> mDataset;
    private List<XmlNotificaciones>  mDatasetOriginal;

    private ArrayList<HashMap<String, String>> data;
    private ArrayList<HashMap<String, String>> originaldata;


    OnItemClickListener mItemClickListener;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView generic_field_id, generic_field_1, generic_field_2, generic_field_3, generic_field_4,generic_field_5;
        private TextView generic_field_6, generic_field_7, generic_field_8, generic_field_9,generic_field_10;
        public ImageView imageView;



        public ViewHolder(View v) {
            super(v);
            generic_field_id = (TextView) v.findViewById(R.id.generic_field_id);
            generic_field_1 = (TextView) v.findViewById(R.id.generic_field_1);
            generic_field_2 = (TextView) v.findViewById(R.id.generic_field_2);
            generic_field_3 = (TextView) v.findViewById(R.id.generic_field_3);
            imageView = (ImageView) v.findViewById(R.id.generic_image);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, getPosition());
        }
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public AdapterNotificaciones(List<XmlNotificaciones> myDataset) {
        mDataset = myDataset;
        mDatasetOriginal = myDataset;
    }

    @Override
    public AdapterNotificaciones.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_notificaciones_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        XmlNotificaciones node = mDataset.get(position);

        //int cantidadTotal = mDataset.size();

        String id_notificacion = node.getId_notificacion();
        String txt_titulo = node.getTxt_titulo();
        String txt_desc = node.getTxt_desc();
        String fec_comp = node.getFec_comp();

        if(node.getImagenB64() != ""){
            String imagenB64 = node.getImagenB64().replace("data:image/jpeg;base64,","");
            byte[] decodedString = Base64.decode(imagenB64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imageView.setImageBitmap(decodedByte);
        }

        holder.generic_field_1.setText("id_notificacion : ".concat(id_notificacion));
        holder.generic_field_2.setText(txt_titulo);
        //holder.generic_field_3.setText(txt_desc);
        //holder.generic_field_4.setText(fec_comp);
        //holder.generic_field_5.setText("Prima Total: ".concat(imagenB64));


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void getFirstFilter(List<XmlNotificaciones> newValues){
        mDataset = newValues;
        getFilter().filter("");
    }


    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                List<XmlNotificaciones> filteredArrayNames = mDataset;

                results.count = filteredArrayNames.size();
                results.values = filteredArrayNames;
                return results;
            }
        };

        return filter;

    }
}