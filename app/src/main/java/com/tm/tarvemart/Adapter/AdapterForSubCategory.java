package com.tm.tarvemart.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tm.tarvemart.R;
import com.tm.tarvemart.entity.SubcategoryData;
import com.tm.tarvemart.inerface.GlobleInterfce;
import com.tm.tarvemart.mainUI.ActivityProduct;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class AdapterForSubCategory extends RecyclerView.Adapter<AdapterForSubCategory.ViewHolder> implements GlobleInterfce {
    Context context;
    List<SubcategoryData> list = new ArrayList<>();

    public AdapterForSubCategory(Context context, List<SubcategoryData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_for_category, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,final int i) {
        Picasso.with(context).load(list.get(i).getCategory_image()).placeholder(R.drawable.logo).into(new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.img_cat.setImageBitmap(bitmap);
                holder.img_cat.setScaleType(ImageView.ScaleType.FIT_XY);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }
        });
        holder.rl_main.setBackgroundColor(context.getResources().getColor(R.color.lightgray));
        holder.txt_catname.setText(list.get(i).getCategory_name());
        holder.iv_openmore.setVisibility(View.GONE);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(i).getCategory_name().equalsIgnoreCase("Kids Fashion")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("This option Coming soon...");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                } else if (list.get(i).getCategory_name().equalsIgnoreCase("Mens Fashion")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("This option Coming soon...");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                } else if (list.get(i).getCategory_name().equalsIgnoreCase("Womens Fashion")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("This option Coming soon...");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                } else {
                    context.startActivity(new Intent(context, ActivityProduct.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("id",list.get(i).getId()));
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_cat, iv_openmore;
        TextView txt_catname;
        RecyclerView rv_Subcategory;
        RelativeLayout rl_main;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_cat = (ImageView) itemView.findViewById(R.id.img_cat);
            iv_openmore = (ImageView) itemView.findViewById(R.id.iv_openmore);
            txt_catname = (TextView) itemView.findViewById(R.id.txt_catname);
            rv_Subcategory=(RecyclerView)itemView.findViewById(R.id.rv_Subcategory);
            rl_main=(RelativeLayout) itemView.findViewById(R.id.rl_main);
        }
    }
}
