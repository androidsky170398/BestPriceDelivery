package com.tm.tarvemart.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tm.tarvemart.R;
import com.tm.tarvemart.entity.ProductData;
import com.tm.tarvemart.mainUI.CartActivity;
import com.tm.tarvemart.mainUI.ProductDetails;
import com.tm.tarvemart.sqliteDatabase.DataSource;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdapterForCart extends RecyclerView.Adapter<AdapterForCart.ViewHolder> {
    Context context;
    List<ProductData> list_Products = new ArrayList<>();
    int count = 0;
    TextView tv_totalRs, tv_totalsave;

    public AdapterForCart(Context context, List<ProductData> list_Products, TextView tv_totalRs, TextView tv_totalsave) {
        this.context = context;
        this.list_Products = list_Products;
        this.tv_totalRs = tv_totalRs;
        this.tv_totalsave = tv_totalsave;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.txt_brandName.setText(list_Products.get(i).getProductname());

        viewHolder.txt_typeName.setText(list_Products.get(i).getType_name());
        viewHolder.txt_quantity.setText(list_Products.get(i).getQuantity_name());
        if (list_Products.get(i).getDiscount().equalsIgnoreCase("0.00")) {
            viewHolder.txt_discount.setVisibility(View.GONE);
        } else {
            viewHolder.txt_discount.setVisibility(View.VISIBLE);
            viewHolder.txt_discount.setText(list_Products.get(i).getDiscount()+" % Off");
        }
        viewHolder.txt_snappelPrice.setText("Rs " + list_Products.get(i).getPrice(), TextView.BufferType.SPANNABLE);
        Spannable spannable = (Spannable) viewHolder.txt_snappelPrice.getText();
        spannable.setSpan(new StrikethroughSpan(), 0, viewHolder.txt_snappelPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.txt_snappelPrice.setTextColor(Color.RED);
        viewHolder.txt_sellingPrice.setText("Rs " + list_Products.get(i).getSelling_price());

        Picasso.with(context).load(list_Products.get(i).getProduct_image())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo).into(viewHolder.img_product);

        /*Picasso.with(context).load(list_Products.get(i).getProduct_image()).placeholder(R.drawable.placeholder).into(new Target() {

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
        viewHolder.txt_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = (Integer.parseInt(viewHolder.txt_prno.getText().toString())) - 1;
                if (count == 0) {
                    viewHolder.btn_add.setVisibility(View.VISIBLE);
                    viewHolder.ll_carditem.setVisibility(View.GONE);
                    DataSource dataSource = new DataSource(context);
                    dataSource.open();
                    ProductData objproductData = dataSource.selectInto_tbl_Products(list_Products.get(i).getId(), viewHolder.txt_quantity.getText().toString().trim()).get(0);
                    if (objproductData != null) {
                        objproductData.setSelected_quantity(count + "");
                        int c = dataSource.deleteInto_tbl_Products(objproductData.getCommon_id());
                        if (c > 0) {
                            list_Products.remove(i);
                            if (list_Products != null && list_Products.size() > 0) {
                                Toast.makeText(context, "Product remove from cart Successfully", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                                CartActivity.totalRsforCart(context, tv_totalRs, tv_totalsave);
                            } else {
                                Intent intent = new Intent(context, CartActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);
                            }
                        } else {
                            Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                    dataSource.close();
                } else if (count > 0) {
                    DataSource dataSource = new DataSource(context);
                    dataSource.open();
                    ProductData objproductData = dataSource.selectInto_tbl_Products(list_Products.get(i).getId(), viewHolder.txt_quantity.getText().toString().trim()).get(0);
                    if (objproductData != null) {
                        objproductData.setSelected_quantity(count + "");
                        int i = dataSource.updateInto_tbl_Products(objproductData.getCommon_id(), objproductData);
                        if (i > 0) {
                            Toast.makeText(context, "Product remove from cart Successfully", Toast.LENGTH_SHORT).show();
                            viewHolder.txt_prno.setText((count) + "");
                            CartActivity.totalRsforCart(context, tv_totalRs, tv_totalsave);
                        } else {
                            Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                    dataSource.close();

                }
            }
        });


        viewHolder.txt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = (Integer.parseInt(viewHolder.txt_prno.getText().toString())) + 1;
                if (count <= Integer.parseInt(list_Products.get(i).getTotal_availability())&&count<=12) {
                    DataSource dataSource = new DataSource(context);
                    dataSource.open();
                    ProductData objproductData = dataSource.selectInto_tbl_Products(list_Products.get(i).getId(), viewHolder.txt_quantity.getText().toString()).get(0);
                    if (objproductData != null) {
                        objproductData.setSelected_quantity(count + "");
                        int i = dataSource.updateInto_tbl_Products(objproductData.getCommon_id(), objproductData);
                        if (i > 0) {
                            Toast.makeText(context, "Product add Successfully", Toast.LENGTH_SHORT).show();
                            viewHolder.txt_prno.setText((count) + "");
                            CartActivity.totalRsforCart(context, tv_totalRs, tv_totalsave);
                        } else {
                            Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                    dataSource.close();
                } else {
                    Toast.makeText(context, "Product Availability gone!!!", Toast.LENGTH_SHORT).show();
                }


            }
        });

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
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetails.class);
                intent.putExtra("ProductDetails", (Serializable) list_Products.get(i));
                intent.putExtra("fromWhere", "Cart");
                intent.putExtra("product_id", list_Products.get(i).getId());
                intent.putExtra("product_quantity", list_Products.get(i).getQuantity_name());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_Products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_brandName, txt_typeName, txt_snappelPrice, txt_sellingPrice, txt_discount, txt_prno;
        TextView txt_quantity;
        ImageView img_product;
        TextView txt_minus, txt_plus;
        Button btn_add;
        LinearLayout ll_carditem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_brandName = itemView.findViewById(R.id.txt_brandName);
            txt_typeName = itemView.findViewById(R.id.txt_typeName);
            txt_snappelPrice = itemView.findViewById(R.id.txt_snappelPrice);
            txt_sellingPrice = itemView.findViewById(R.id.txt_sellingPrice);
            txt_discount = itemView.findViewById(R.id.txt_discount);
            txt_quantity = itemView.findViewById(R.id.txt_quantity);
            img_product = itemView.findViewById(R.id.img_product);
            txt_minus = itemView.findViewById(R.id.txt_minus);
            txt_plus = itemView.findViewById(R.id.txt_plus);
            txt_prno = itemView.findViewById(R.id.txt_prno);
            btn_add = itemView.findViewById(R.id.btn_add);
            ll_carditem = itemView.findViewById(R.id.ll_carditem);
        }
    }
}
