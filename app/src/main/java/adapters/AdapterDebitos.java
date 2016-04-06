package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import models.XmlDebitos;

/**
 * Created by mac on 28/3/16.
 */
public class AdapterDebitos  extends RecyclerView.Adapter<AdapterDebitos.ViewHolder> {

    private List<XmlDebitos> mDataset;
    private List<XmlDebitos>  mDatasetOriginal;

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
            //generic_field_id = (TextView) v.findViewById(R.id.generic_field_id);
            generic_field_1 = (TextView) v.findViewById(R.id.TextBoxValue1);
            generic_field_2 = (TextView) v.findViewById(R.id.TextBoxValue2);
            generic_field_3 = (TextView) v.findViewById(R.id.TextBoxValue3);
            generic_field_4 = (TextView) v.findViewById(R.id.TextBoxValue4);
            generic_field_5 = (TextView) v.findViewById(R.id.TextBoxValue5);
            generic_field_6 = (TextView) v.findViewById(R.id.TextBoxValue6);
            generic_field_7 = (TextView) v.findViewById(R.id.TextBoxValue7);
            generic_field_8 = (TextView) v.findViewById(R.id.TextBoxValue8);
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


    public AdapterDebitos(List<XmlDebitos> myDataset) {
        mDataset = myDataset;
        mDatasetOriginal = myDataset;
    }

    @Override
    public AdapterDebitos.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_item_titulo_valor_debitos, parent, false);
        ViewHolder vh = new ViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        XmlDebitos node = mDataset.get(position);

        //int cantidadTotal = mDataset.size();

        String nro_cta_tarj = node.getNro_cta_tarj();
        String nro_cuota = node.getNro_cuota();
        String nro_comprobante = node.getNro_comprobante();
        String fecha_generado = node.getFecha_generado();
        String prima_total = node.getPrima_total();
        String prima_pagada = node.getPrima_pagada();
        String prima_vencida = node.getPrima_vencida();
        String txt_estado_intento = node.getTxt_estado_intento();


        holder.generic_field_1.setText(nro_cta_tarj);
        holder.generic_field_2.setText(nro_cuota);
        holder.generic_field_3.setText(nro_comprobante);
        holder.generic_field_4.setText(fecha_generado);
        holder.generic_field_5.setText(prima_total);
        holder.generic_field_6.setText(prima_pagada);
        holder.generic_field_7.setText(prima_vencida);
        holder.generic_field_8.setText(txt_estado_intento);


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void getFirstFilter(List<XmlDebitos> newValues){
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
                List<XmlDebitos> filteredArrayNames = mDataset;

                results.count = filteredArrayNames.size();
                results.values = filteredArrayNames;
                return results;
            }
        };

        return filter;

    }
}