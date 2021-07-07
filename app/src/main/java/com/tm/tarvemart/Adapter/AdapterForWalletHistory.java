package com.tm.tarvemart.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tm.tarvemart.R;
import com.tm.tarvemart.entity.WalletData;

import java.util.ArrayList;
import java.util.List;

public class AdapterForWalletHistory extends RecyclerView.Adapter<AdapterForWalletHistory.VH> {
    Context context;
    List<WalletData> list_WalletData = new ArrayList<>();

    public AdapterForWalletHistory(Context context, List<WalletData> list_WalletData) {
        this.context = context;
        this.list_WalletData = list_WalletData;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_for_wallethistory, viewGroup, false);
        VH viewHolder = new VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        vh.txt_details.setText(Html.fromHtml(list_WalletData.get(i).getRemark()));
        vh.txt_datetime.setText(Html.fromHtml(list_WalletData.get(i).getTransaction_date()));
        //   Toast.makeText(context, arrayList.get(i).get("amount"), Toast.LENGTH_SHORT).show();
        vh.txt_rs.setText(Html.fromHtml(list_WalletData.get(i).getAmount()));

        String type = list_WalletData.get(i).getType();
        if (type.equalsIgnoreCase("credit")) {
            vh.img_icon.setImageResource(R.drawable.ic_wallet_black);
        } else {
            vh.img_icon.setImageResource(R.drawable.ic_wallet_black);
        }


    }

    @Override
    public int getItemCount() {
        return list_WalletData.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView txt_details, txt_datetime, txt_rs;
        ImageView img_icon;

        public VH(@NonNull View itemView) {
            super(itemView);
            txt_details = itemView.findViewById(R.id.txt_details);
            txt_datetime = itemView.findViewById(R.id.txt_datetime);
            txt_rs = itemView.findViewById(R.id.txt_rs);
            img_icon = itemView.findViewById(R.id.img_icon);
        }
    }
}
