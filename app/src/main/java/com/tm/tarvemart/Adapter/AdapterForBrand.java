package com.tm.tarvemart.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.tm.tarvemart.R;
import com.tm.tarvemart.entity.BrandData;
import com.tm.tarvemart.inerface.GlobleInterfce;

import java.util.ArrayList;
import java.util.List;

public class AdapterForBrand extends RecyclerView.Adapter<AdapterForBrand.ViewHolder> implements GlobleInterfce {
    Context context;
    List<BrandData> list = new ArrayList<>();


    public AdapterForBrand(Context context, List<BrandData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_for_brand, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        holder.ch_brand.setText(list.get(i).getBrandname());
        holder.ch_brand.setChecked(false);
        holder.ch_brand.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    list.get(i).setChecked(true);
                } else {
                    list.get(i).setChecked(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox ch_brand;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ch_brand = (CheckBox) itemView.findViewById(R.id.ch_brand);
        }
    }
}
