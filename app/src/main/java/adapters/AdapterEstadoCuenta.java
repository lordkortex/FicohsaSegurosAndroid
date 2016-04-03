package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;


import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.hn.com.ficohsaseguros.R;
import interfaces.OnItemClickListener;
import models.XmlEstadoCuenta;
import models.XmlTokenLoginGestiones;


/**
 * Created by mac on 11/10/15.
 */
public class AdapterEstadoCuenta extends RecyclerView.Adapter<AdapterEstadoCuenta.ViewHolder> {

    private List<XmlEstadoCuenta> mDataset;
    private List<XmlEstadoCuenta>  mDatasetOriginal;

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
            generic_field_4 = (TextView) v.findViewById(R.id.generic_field_4);
            generic_field_5 = (TextView) v.findViewById(R.id.generic_field_5);
            generic_field_6 = (TextView) v.findViewById(R.id.generic_field_6);
            generic_field_7 = (TextView) v.findViewById(R.id.generic_field_7);
            generic_field_8 = (TextView) v.findViewById(R.id.generic_field_8);
            generic_field_9 = (TextView) v.findViewById(R.id.generic_field_9);
            generic_field_10= (TextView) v.findViewById(R.id.generic_field_10);
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


    public AdapterEstadoCuenta(List<XmlEstadoCuenta> myDataset) {
        mDataset = myDataset;
        mDatasetOriginal = myDataset;
    }

    @Override
    public AdapterEstadoCuenta.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_generico_item_estado_cuenta, parent, false);
        ViewHolder vh = new ViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        XmlEstadoCuenta node = mDataset.get(position);

        //int cantidadTotal = mDataset.size();

        String nro_cuota = node.getNro_cuota();
        String nro_comprobante = node.getNro_comprobante();
        String fecha_vencimiento = node.getFecha_vencimiento();
        String imp_prima_total = node.getImp_prima_total();
        String fecha_proceso = node.getFecha_proceso();
        String txt_estado = node.getTxt_estado();
        String imp_prima_deposito = node.getImp_prima_deposito();
        String imp_prima_pagada = node.getImp_prima_pagada();
        String txt_moneda = node.getTxt_moneda();
        String fecha_cobranza = node.getFecha_cobranza();

        holder.generic_field_1.setText("Numero de Cuota : ".concat(nro_cuota));
        holder.generic_field_2.setText("Numero de Recibo : ".concat(nro_comprobante));
        holder.generic_field_3.setText("Fecha Vencimiento : ".concat(fecha_vencimiento));
        holder.generic_field_4.setText("Prima Total : ".concat(imp_prima_total));
        holder.generic_field_5.setText("Fecha Proceso : ".concat(fecha_proceso));
        holder.generic_field_6.setText("Estado : ".concat(txt_estado));
        holder.generic_field_7.setText("Prima Deposito : ".concat(imp_prima_deposito));
        holder.generic_field_8.setText("Prima Pagada : ".concat(imp_prima_pagada));
        holder.generic_field_9.setText("Moneda : ".concat(txt_moneda));
        holder.generic_field_10.setText("Fecha Cobranza : ".concat(fecha_cobranza));


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void getFirstFilter(List<XmlEstadoCuenta> newValues){
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
                List<XmlEstadoCuenta> filteredArrayNames = mDataset;

                results.count = filteredArrayNames.size();
                results.values = filteredArrayNames;
                return results;
            }
        };

        return filter;

    }
}