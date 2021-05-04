package com.ourdevelops.ourdriver.item;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ourdevelops.ourdriver.R;
import com.ourdevelops.ourdriver.models.WalletModel;
import com.ourdevelops.ourdriver.utils.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by otacodes on 3/24/2019.
 */

public class WalletItem extends RecyclerView.Adapter<WalletItem.ItemRowHolder> {

    private List<WalletModel> dataList;
    private Context mContext;
    private int rowLayout;


    public WalletItem(Context context, List<WalletModel> dataList, int rowLayout) {
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
        final WalletModel singleItem = dataList.get(position);


        if (singleItem.getType().equals("withdraw")) {
            holder.text.setText(singleItem.getType());
            Utility.currencyTXT(holder.nominal, singleItem.getJumlah(), mContext);

            if (singleItem.getStatus().equals("1")) {
                holder.text.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.nominal.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.background.setColorFilter(mContext.getResources().getColor(R.color.red));
                holder.keterangan.setText("Money Out");
            } else if (singleItem.getStatus().equals("0")) {
                holder.text.setTextColor(mContext.getResources().getColor(R.color.yellow));
                holder.nominal.setTextColor(mContext.getResources().getColor(R.color.yellow));
                holder.background.setColorFilter(mContext.getResources().getColor(R.color.yellow));
                holder.keterangan.setText("Pending");
            } else if (singleItem.getStatus().equals("2")) {
                holder.text.setTextColor(mContext.getResources().getColor(R.color.gray));
                holder.nominal.setTextColor(mContext.getResources().getColor(R.color.gray));
                holder.background.setColorFilter(mContext.getResources().getColor(R.color.gray));
                holder.keterangan.setText("Cancel");
            }

        } else if (singleItem.getType().equals("Order-")) {
            holder.text.setText("Order " + singleItem.getBank());
            Utility.currencyTXT(holder.nominal, singleItem.getJumlah(), mContext);

            if (singleItem.getStatus().equals("1")) {
                holder.text.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.nominal.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.background.setColorFilter(mContext.getResources().getColor(R.color.red));
                holder.keterangan.setText("Money Out");
            } else if (singleItem.getStatus().equals("0")) {
                holder.text.setTextColor(mContext.getResources().getColor(R.color.yellow));
                holder.nominal.setTextColor(mContext.getResources().getColor(R.color.yellow));
                holder.background.setColorFilter(mContext.getResources().getColor(R.color.yellow));
                holder.keterangan.setText("Pending");
            } else if (singleItem.getStatus().equals("2")) {
                holder.text.setTextColor(mContext.getResources().getColor(R.color.gray));
                holder.nominal.setTextColor(mContext.getResources().getColor(R.color.gray));
                holder.background.setColorFilter(mContext.getResources().getColor(R.color.gray));
                holder.keterangan.setText("Cancel");
            }

        } else if (singleItem.getType().equals("topup") && singleItem.getStatus().equals("1")) {
            holder.text.setText(singleItem.getType());
            Utility.currencyTXT(holder.nominal, singleItem.getJumlah(), mContext);
            holder.text.setTextColor(mContext.getResources().getColor(R.color.green));
            holder.nominal.setTextColor(mContext.getResources().getColor(R.color.green));
            holder.background.setColorFilter(mContext.getResources().getColor(R.color.green));
            holder.keterangan.setText("Money In");

        } else if (singleItem.getType().equals("Order+")) {
            holder.text.setText("Order " + singleItem.getBank());
            Utility.currencyTXT(holder.nominal, singleItem.getJumlah(), mContext);
            holder.text.setTextColor(mContext.getResources().getColor(R.color.green));
            holder.nominal.setTextColor(mContext.getResources().getColor(R.color.green));
            holder.background.setColorFilter(mContext.getResources().getColor(R.color.green));
            holder.keterangan.setText("Money In");

        } else {
            holder.background.setColorFilter(mContext.getResources().getColor(R.color.yellow));
            holder.text.setText(singleItem.getType());
            holder.text.setTextColor(mContext.getResources().getColor(R.color.yellow));
            holder.nominal.setTextColor(mContext.getResources().getColor(R.color.yellow));
            Utility.currencyTXT(holder.nominal, singleItem.getJumlah(), mContext);

            holder.keterangan.setText("Pending");


        }
        Date myDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.US);
        try {
            myDate = dateFormat.parse(singleItem.getWaktu());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        String finalDate = timeFormat.format(myDate);
        holder.tanggal.setText(finalDate);
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
        TextView text, tanggal, nominal, keterangan;
        ImageView background;

        ItemRowHolder(View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.background);
            text = itemView.findViewById(R.id.text);
            tanggal = itemView.findViewById(R.id.texttanggal);
            nominal = itemView.findViewById(R.id.textharga);
            keterangan = itemView.findViewById(R.id.textket);
        }
    }


}
