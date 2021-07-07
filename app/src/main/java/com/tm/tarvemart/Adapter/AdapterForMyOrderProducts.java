package com.tm.tarvemart.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tm.tarvemart.R;
import com.tm.tarvemart.entity.ProductData;
import com.tm.tarvemart.sqliteDatabase.DataSource;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterForMyOrderProducts extends RecyclerView.Adapter<AdapterForMyOrderProducts.ViewHolder> {
    Context context;
    List<ProductData> list_Products = new ArrayList<>();
    int count = 0;

    public AdapterForMyOrderProducts(Context context, List<ProductData> list_Products) {
        this.context = context;
        this.list_Products = list_Products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_for_my_order_products, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.txt_brandName.setText(list_Products.get(i).getProduct_name());
        viewHolder.txt_quantity.setText(list_Products.get(i).getProduct_quantity_name());
        viewHolder.txt_sellingPrice.setText("Rs " + list_Products.get(i).getSelling_price());
        viewHolder.btn_add.setText("Qty : " + list_Products.get(i).getSelected_quantity());
        Picasso.with(context).load(list_Products.get(i).getProduct_img_link())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo).into(viewHolder.img_product);
        /*Picasso.with(context).load(list_Products.get(i).getProduct_img_link()).placeholder(R.drawable.placeholder).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                viewHolder.img_product.setImageBitmap(bitmap);
                viewHolder.img_product.setScaleType(ImageView.ScaleType.FIT_XY);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }
        });*/

        viewHolder.txt_prno.setText(count + "");
        DataSource dataSource = new DataSource(context);
        dataSource.open();
        ProductData objproductData = null;
        try {
            objproductData = dataSource.selectInto_tbl_Products(list_Products.get(i).getId(), viewHolder.txt_quantity.getText().toString()).get(0);
        } catch (Exception e) {
            objproductData = null;
        }
        if (objproductData != null) {
            viewHolder.btn_add.setVisibility(View.GONE);
            viewHolder.ll_carditem.setVisibility(View.VISIBLE);
            viewHolder.txt_prno.setText(objproductData.getSelected_quantity());
        } else {
            viewHolder.btn_add.setVisibility(View.VISIBLE);
            viewHolder.ll_carditem.setVisibility(View.GONE);
        }
        dataSource.close();
        /*viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetails.class);
                intent.putExtra("ProductDetails", (Serializable) list_Products.get(i));
                intent.putExtra("fromWhere", "MyOrder");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return list_Products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_brandName, txt_ProductCount, txt_sellingPrice, txt_discount, txt_prno;
        TextView txt_quantity;
        ImageView img_product;
        Button btn_add;
        LinearLayout ll_carditem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_brandName = itemView.findViewById(R.id.txt_brandName);
            txt_ProductCount = itemView.findViewById(R.id.txt_ProductCount);
            txt_sellingPrice = itemView.findViewById(R.id.txt_sellingPrice);
            txt_discount = itemView.findViewById(R.id.txt_discount);
            txt_quantity = itemView.findViewById(R.id.txt_quantity);
            img_product = itemView.findViewById(R.id.img_product);
            txt_prno = itemView.findViewById(R.id.txt_prno);
            btn_add = itemView.findViewById(R.id.btn_add);
            ll_carditem = itemView.findViewById(R.id.ll_carditem);
        }
    }
}
