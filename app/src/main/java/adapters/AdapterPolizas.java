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

import app.hn.com.ficohsaseguros.R;
import interfaces.OnItemClickListener;
import models.XmlTokenLoginResult;
import models.XmlTokenLoginResultItems;


/**
 * Created by mac on 11/10/15.
 */
public class AdapterPolizas extends RecyclerView.Adapter<AdapterPolizas.ViewHolder> {

    private XmlTokenLoginResult mDataset;
    private XmlTokenLoginResult mDatasetOriginal;

    private ArrayList<HashMap<String, String>> data;
    private ArrayList<HashMap<String, String>> originaldata;


    OnItemClickListener mItemClickListener;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView generic_field_id, generic_field_1, generic_field_2, generic_field_3, generic_field_4,generic_field_5,generic_field_6,generic_field_7,generic_field_8;
        private TextView generic_field_9, generic_field_10, generic_field_11, generic_field_12,generic_field_13,generic_field_14,generic_field_15;

        public ImageView imageView;



        public ViewHolder(View v) {
            super(v);
            //generic_field_id = (TextView) v.findViewById(R.id.TextBoxValue1);
            generic_field_1 = (TextView) v.findViewById(R.id.TextBoxValue1);
            generic_field_2 = (TextView) v.findViewById(R.id.TextBoxValue2);
            generic_field_3 = (TextView) v.findViewById(R.id.TextBoxValue3);
            generic_field_4 = (TextView) v.findViewById(R.id.TextBoxValue4);
            generic_field_5= (TextView) v.findViewById(R.id.TextBoxValue5);
            generic_field_6 = (TextView) v.findViewById(R.id.TextBoxValue6);
            generic_field_7 = (TextView) v.findViewById(R.id.TextBoxValue7);
            generic_field_8= (TextView) v.findViewById(R.id.TextBoxValue8);

            generic_field_9= (TextView) v.findViewById(R.id.TextBoxValue9);
            generic_field_10= (TextView) v.findViewById(R.id.TextBoxValue10);
            generic_field_11= (TextView) v.findViewById(R.id.TextBoxValue11);
            generic_field_12= (TextView) v.findViewById(R.id.TextBoxValue12);
            generic_field_13= (TextView) v.findViewById(R.id.TextBoxValue13);
            generic_field_14= (TextView) v.findViewById(R.id.TextBoxValue14);
            generic_field_15= (TextView) v.findViewById(R.id.TextBoxValue15);

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


    public AdapterPolizas(XmlTokenLoginResult myDataset) {
        mDataset = myDataset;
        mDatasetOriginal = myDataset;
    }

    @Override
    public AdapterPolizas.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_item_titulo_valor, parent, false);
        ViewHolder vh = new ViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        /*holder.generic_field_1.setText("Poliza : ".concat(mDataset.getNro_pol()));
        holder.generic_field_2.setText("Ramo : ".concat(mDataset.getTxt_ramo()));
        holder.generic_field_3.setText("Sucursal :".concat(mDataset.getTxt_suc()));
        holder.generic_field_4.setText("Año : ".concat(mDataset.getAnio_pol()));
        holder.generic_field_5.setText("Estado : ".concat(mDataset.getTxt_estado_pol()));
        holder.generic_field_6.setText("Agente : ".concat("PENTINTE"));
        holder.generic_field_7.setText("Contratante : ".concat(mDataset.getTxt_contratante()));
        holder.generic_field_8.setText("Vigencia : ".concat("PENDIENTE"));*/

        holder.generic_field_1.setText(mDataset.getNro_pol());
        holder.generic_field_2.setText(mDataset.getTxt_ramo());
        holder.generic_field_3.setText(mDataset.getTxt_suc());
        holder.generic_field_4.setText(mDataset.getAnio_pol());
        holder.generic_field_5.setText(mDataset.getTxt_estado_pol());
        holder.generic_field_6.setText(mDataset.getTxt_agente());
        holder.generic_field_7.setText(mDataset.getTxt_contratante());
        holder.generic_field_8.setText(mDataset.getFec_vig_desde() + " / " + mDataset.getFec_vig_hasta());


        if(!mDataset.getXmlTokenLoginResultItemsList().isEmpty()){
            XmlTokenLoginResultItems xmlTokenLoginResultItems =  mDataset.getXmlTokenLoginResultItemsList().get(0);

            holder.generic_field_9.setText(xmlTokenLoginResultItems.getTxt_chasis());
            holder.generic_field_10.setText(xmlTokenLoginResultItems.getTxt_motor());
            holder.generic_field_11.setText(xmlTokenLoginResultItems.getTxt_placa());
            holder.generic_field_12.setText(xmlTokenLoginResultItems.getTxt_marca());
            holder.generic_field_13.setText(xmlTokenLoginResultItems.getTxt_modelo());
            holder.generic_field_14.setText(xmlTokenLoginResultItems.getTxt_color());
            holder.generic_field_15.setText(xmlTokenLoginResultItems.getAaaa_modelo());

        }



    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void getFirstFilter(XmlTokenLoginResult newValues){
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
                XmlTokenLoginResult filteredArrayNames = mDataset;

                //results.count = filteredArrayNames.getLength();
                //results.values = filteredArrayNames;
                return results;
            }
        };

        return filter;

    }
}