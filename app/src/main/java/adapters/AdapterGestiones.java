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
import models.XmlTokenLoginGestiones;

/**
 * Created by mac on 1/11/15.
 */
public class AdapterGestiones  extends RecyclerView.Adapter<AdapterGestiones.ViewHolder> {

    private List<XmlTokenLoginGestiones> mDataset;
    private List<XmlTokenLoginGestiones>  mDatasetOriginal;

    private ArrayList<HashMap<String, String>> data;
    private ArrayList<HashMap<String, String>> originaldata;


    OnItemClickListener mItemClickListener;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView generic_field_id, generic_field_1, generic_field_2, generic_field_3, generic_field_4,generic_field_5;
        public ImageView imageView;



        public ViewHolder(View v) {
            super(v);
            generic_field_id = (TextView) v.findViewById(R.id.generic_field_id);
            generic_field_1 = (TextView) v.findViewById(R.id.generic_field_1);
            generic_field_2 = (TextView) v.findViewById(R.id.generic_field_2);
            generic_field_3 = (TextView) v.findViewById(R.id.generic_field_3);
            generic_field_4 = (TextView) v.findViewById(R.id.generic_field_4);
            generic_field_5= (TextView) v.findViewById(R.id.generic_field_5);
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


    public AdapterGestiones(List<XmlTokenLoginGestiones>  myDataset) {
        mDataset = myDataset;
        mDatasetOriginal = myDataset;
    }

    @Override
    public AdapterGestiones.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_notificaciones_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        XmlTokenLoginGestiones node = mDataset.get(position);

        holder.generic_field_1.setText(node.getFec_evento());
        holder.generic_field_2.setText(node.getTxt_obs());



    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void getFirstFilter(List<XmlTokenLoginGestiones> newValues){
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
                List<XmlTokenLoginGestiones> filteredArrayNames = mDataset;

                results.count = filteredArrayNames.size();
                results.values = filteredArrayNames;
                return results;
            }
        };

        return filter;

    }
}