package com.ourdevelops.ourdriver.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ourdevelops.ourdriver.R;
import com.ourdevelops.ourdriver.models.ItemPesananModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ourdevelops team on 3/24/2019.
 */

public class ItemPesananItem extends RecyclerView.Adapter<ItemPesananItem.ItemRowHolder> {

    private List<ItemPesananModel> dataList;
    private Context mContext;
    private int rowLayout;

    public ItemPesananItem(Context context, List<ItemPesananModel> dataList, int rowLayout) {
        this.dataList = dataList;
        this.mContext = context;
        this.rowLayout = rowLayout;
    }

    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemRowHolder holder, final int position) {
        final ItemPesananModel singleItem = dataList.get(position);
        holder.name.setText(singleItem.getNama_item());
        holder.qty.setText(singleItem.getJumlah_item());
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
        TextView name,qty;

        ItemRowHolder(View itemView) {
            super(itemView);
            qty = itemView.findViewById(R.id.qty);
            name = itemView.findViewById(R.id.namaitem);

        }
    }
}
