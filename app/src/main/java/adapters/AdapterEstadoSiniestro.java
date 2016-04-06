package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Xml;
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
import models.XmlSiniestros;


/**
 * Created by mac on 11/10/15.
 */
public class AdapterEstadoSiniestro extends RecyclerView.Adapter<AdapterEstadoSiniestro.ViewHolder> {

    private List<XmlSiniestros> mDataset;
    private List<XmlSiniestros>  mDatasetOriginal;

    private ArrayList<HashMap<String, String>> data;
    private ArrayList<HashMap<String, String>> originaldata;


    OnItemClickListener mItemClickListener;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView generic_field_id, generic_field_1, generic_field_2, generic_field_3, generic_field_4,generic_field_5;
        public ImageView imageView;



        public ViewHolder(View v) {
            super(v);
            //generic_field_id = (TextView) v.findViewById(R.id.generic_field_id);
            generic_field_1 = (TextView) v.findViewById(R.id.TextBoxValue1);
            generic_field_2 = (TextView) v.findViewById(R.id.TextBoxValue2);
            generic_field_3 = (TextView) v.findViewById(R.id.TextBoxValue3);
            generic_field_4 = (TextView) v.findViewById(R.id.TextBoxValue4);
            generic_field_5= (TextView) v.findViewById(R.id.TextBoxValue5);
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


    public AdapterEstadoSiniestro(List<XmlSiniestros>  myDataset) {
        mDataset = myDataset;
        mDatasetOriginal = myDataset;
    }

    @Override
    public AdapterEstadoSiniestro.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_item_titulo_valor_estado_siniestro, parent, false);
        ViewHolder vh = new ViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        XmlSiniestros node = mDataset.get(position);


        holder.generic_field_1.setText(node.getNro_siniestro());
        holder.generic_field_2.setText(node.getTxt_suc());
        holder.generic_field_3.setText(node.getTxt_ramo());
        holder.generic_field_4.setText(node.getNro_pol());
        holder.generic_field_5.setText(node.getTxt_contratante());



    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void getFirstFilter(List<XmlSiniestros>  newValues){
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
                List<XmlSiniestros>  filteredArrayNames = mDataset;

                results.count = filteredArrayNames.size();
                results.values = filteredArrayNames;
                return results;
            }
        };

        return filter;

    }
}