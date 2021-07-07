package com.tm.tarvemart.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tm.tarvemart.R;
import com.tm.tarvemart.entity.CityPriceData;
import com.tm.tarvemart.entity.ProductData;
import com.tm.tarvemart.mainUI.ActivityProduct;
import com.tm.tarvemart.mainUI.ProductDetails;
import com.tm.tarvemart.sqliteDatabase.DataSource;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class AdapterForProduct extends RecyclerView.Adapter<AdapterForProduct.VH> {
    Context context;
    List<ProductData> productData;
    int count = 1;
    private String abc;

    public AdapterForProduct(ActivityProduct activityProduct, List<ProductData> productData) {
        this.context = activityProduct;
        this.productData = productData;
    }

    @NonNull
    @Override
    public AdapterForProduct.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dynamic_product, viewGroup, false);
        VH vh = new VH(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterForProduct.VH vh, final int i) {
        vh.txt_brandName.setText(productData.get(i).getProductname());
        vh.txt_typeName.setText(productData.get(i).getType_name());
        Picasso.with(context).load(productData.get(i).getProduct_image()).fit()
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo).into(vh.img_product);

        if (productData.get(i).getCityprice() != null && productData.get(i).getCityprice().size() > 0) {
            final List<CityPriceData> priceDataList = productData.get(i).getCityprice();
            String[] arrQuantity = new String[priceDataList.size()];
            HashMap<String, String> hasmapForQuantity = new HashMap<>();
            for (int j = 0; j < priceDataList.size(); j++) {
                arrQuantity[j] = priceDataList.get(j).getQuantity_name();
                hasmapForQuantity.put(priceDataList.get(j).getQuantity_name(), priceDataList.get(j).getQuantity_id());
            }
            vh.sp_quantityName.setAdapter(new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, arrQuantity));

            vh.btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (vh.btn_add.getText().toString().equalsIgnoreCase("Add")) {
                        int qty = 0;
                        if (priceDataList != null && priceDataList.size() > 0) {
                            for (int j = 0; j < priceDataList.size(); j++) {
                                if (vh.sp_quantityName.getSelectedItem().toString().trim().equalsIgnoreCase(priceDataList.get(j).getQuantity_name().trim())) {
                                    qty = Integer.parseInt(priceDataList.get(j).getTotal_availability());
                                }
                            }
                        }
                        if (qty > 0) {
                            vh.btn_add.setVisibility(View.GONE);
                            vh.ll_carditem.setVisibility(View.VISIBLE);
                            ProductData obj_productDataobj = new ProductData();
                            obj_productDataobj.setId(productData.get(i).getId());
                            obj_productDataobj.setProductname(productData.get(i).getProductname());
                            obj_productDataobj.setGst(productData.get(i).getGst());
                            obj_productDataobj.setSku(productData.get(i).getSku());
                            obj_productDataobj.setDescription(productData.get(i).getDescription());
                            obj_productDataobj.setType_name_id(productData.get(i).getType_name_id());
                            obj_productDataobj.setType_name(productData.get(i).getType_name());
                            obj_productDataobj.setProduct_about(productData.get(i).getProduct_about());
                            obj_productDataobj.setProduct_ingredient(productData.get(i).getProduct_ingredient());
                            obj_productDataobj.setProductinfo(productData.get(i).getProductinfo());
                            obj_productDataobj.setStatus(productData.get(i).getStatus());
                            obj_productDataobj.setCreated_by(productData.get(i).getCreated_by());
                            obj_productDataobj.setShop_id(productData.get(i).getShop_id());
                            obj_productDataobj.setBrand_id(productData.get(i).getBrand_id());
                            obj_productDataobj.setBrandname(productData.get(i).getBrandname());
                            obj_productDataobj.setBrand_image(productData.get(i).getBrand_image());
                            obj_productDataobj.setProduct_image(productData.get(i).getProduct_image());
                            /*For price*/
                            if (priceDataList != null && priceDataList.size() > 0) {
                                for (int j = 0; j < priceDataList.size(); j++) {
                                    if (vh.sp_quantityName.getSelectedItem().toString().trim().equalsIgnoreCase(priceDataList.get(j).getQuantity_name().trim())) {
                                        obj_productDataobj.setCity_id(priceDataList.get(j).getCity_id());
                                        obj_productDataobj.setQuantity_id(priceDataList.get(j).getQuantity_id());
                                        obj_productDataobj.setPrice(priceDataList.get(j).getPrice());
                                        obj_productDataobj.setSelling_price(priceDataList.get(j).getSelling_price());
                                        obj_productDataobj.setDiscount(priceDataList.get(j).getDiscount());
                                        obj_productDataobj.setTotal_availability(priceDataList.get(j).getTotal_availability());
                                        obj_productDataobj.setQuantity_name(priceDataList.get(j).getQuantity_name());
                                        obj_productDataobj.setSelected_quantity("1");
                                        obj_productDataobj.setCity_name(priceDataList.get(j).getCity_name());
                                        break;
                                    }
                                }
                            }
                            DataSource dataSource = new DataSource(context);
                            dataSource.open();
                            long c = dataSource.insertInto_tbl_Products(obj_productDataobj);
                            dataSource.close();
                            if (c > 0) {
                                vh.txt_prno.setText("1");
                                Toast.makeText(context, "Product added successfully!!!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "some error occurred", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Product currently Not Available!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });


            vh.txt_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count = (Integer.parseInt(vh.txt_prno.getText().toString())) - 1;
                    if (count == 0) {
                        vh.btn_add.setVisibility(View.VISIBLE);
                        vh.ll_carditem.setVisibility(View.GONE);
                        if (priceDataList != null && priceDataList.size() > 0) {
                            for (int j = 0; j < priceDataList.size(); j++) {
                                if (vh.sp_quantityName.getSelectedItem().toString().trim().equalsIgnoreCase(priceDataList.get(j).getQuantity_name().trim())) {
                                    DataSource dataSource = new DataSource(context);
                                    dataSource.open();
                                    ProductData objproductData = dataSource.selectInto_tbl_Products(productData.get(i).getId(), vh.sp_quantityName.getSelectedItem().toString()).get(0);
                                    if (objproductData != null) {
                                        objproductData.setSelected_quantity(count + "");
                                        int i = dataSource.deleteInto_tbl_Products(objproductData.getCommon_id());
                                        if (i > 0) {
                                            Toast.makeText(context, "Product remove from cart Successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    dataSource.close();
                                    break;
                                }
                            }
                        }
                    } else if (count > 0) {
                        if (priceDataList != null && priceDataList.size() > 0) {
                            for (int j = 0; j < priceDataList.size(); j++) {
                                if (vh.sp_quantityName.getSelectedItem().toString().trim().equalsIgnoreCase(priceDataList.get(j).getQuantity_name().trim())) {
                                    DataSource dataSource = new DataSource(context);
                                    dataSource.open();
                                    ProductData objproductData = dataSource.selectInto_tbl_Products(productData.get(i).getId(), vh.sp_quantityName.getSelectedItem().toString()).get(0);
                                    if (objproductData != null) {
                                        objproductData.setSelected_quantity(count + "");
                                        int i = dataSource.updateInto_tbl_Products(objproductData.getCommon_id(), objproductData);
                                        if (i > 0) {
                                            Toast.makeText(context, "Product remove from cart Successfully", Toast.LENGTH_SHORT).show();
                                            vh.txt_prno.setText((count) + "");
                                        } else {
                                            Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    dataSource.close();
                                    break;
                                }
                            }
                        }

                    }
                }
            });


            vh.txt_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count = (Integer.parseInt(vh.txt_prno.getText().toString())) + 1;
                    Toast.makeText(context, "count:"+count, Toast.LENGTH_SHORT).show();
                    if (priceDataList != null && priceDataList.size() > 0) {
                        for (int j = 0; j < priceDataList.size(); j++) {
                            if (vh.sp_quantityName.getSelectedItem().toString().trim().equalsIgnoreCase(priceDataList.get(j).getQuantity_name().trim())) {
                                if (count <= Integer.parseInt(priceDataList.get(j).getTotal_availability())) {
                                    DataSource dataSource = new DataSource(context);
                                    dataSource.open();
                                    ProductData objproductData = dataSource.selectInto_tbl_Products(productData.get(i).getId(), vh.sp_quantityName.getSelectedItem().toString()).get(0);
                                    if (objproductData != null) {
                                        objproductData.setSelected_quantity(count + "");
                                        int i = dataSource.updateInto_tbl_Products(objproductData.getCommon_id(), objproductData);
                                        if (i > 0) {
                                            Toast.makeText(context, "Product add Successfully", Toast.LENGTH_SHORT).show();
                                            vh.txt_prno.setText((count) + "");
                                        } else {
                                            Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    dataSource.close();
                                } else {
                                    Toast.makeText(context, "Product Availability gone!!!", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                        }
                    }
                }
            });
            vh.txt_prno.setText(count + "");

            /*if (priceDataList != null && priceDataList.size() > 0) {
                for (int j = 0; j < priceDataList.size(); j++) {
                    if (vh.sp_quantityName.getSelectedItem().toString().trim().equalsIgnoreCase(priceDataList.get(j).getQuantity_name().trim())) {
                        DataSource dataSource = new DataSource(context);
                        dataSource.open();
                        ProductData objproductData = null;
                        try {
                            objproductData = dataSource.selectInto_tbl_Products(productData.get(i).getId(), vh.sp_quantityName.getSelectedItem().toString()).get(0);
                        } catch (Exception e) {
                            objproductData = null;
                        }
                        if (objproductData != null) {
                            vh.btn_add.setVisibility(View.GONE);
                            vh.ll_carditem.setVisibility(View.VISIBLE);
                            vh.txt_prno.setText(objproductData.getSelected_quantity());
                        } else {
                            vh.btn_add.setVisibility(View.VISIBLE);
                            vh.ll_carditem.setVisibility(View.GONE);
                        }
                        dataSource.close();
                        break;
                    }else{
                        Log.e("","");
                    }
                }
            }*/

            vh.sp_quantityName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    for (int j = 0; j < priceDataList.size(); j++) {
                        if (vh.sp_quantityName.getSelectedItem().toString().trim().equalsIgnoreCase(priceDataList.get(j).getQuantity_name())) {
                            vh.txt_discount.setText(priceDataList.get(j).getDiscount()+" % Off");
                            String discount = priceDataList.get(j).getDiscount();
                            if (discount.equalsIgnoreCase("0.00")) {
                                vh.txt_discount.setVisibility(View.GONE);
                            }
                            vh.txt_snappelPrice.setText(priceDataList.get(j).getPrice(),TextView.BufferType.SPANNABLE);
                            Spannable spannable = (Spannable) vh.txt_snappelPrice.getText();
                            spannable.setSpan(new StrikethroughSpan(), 0, vh.txt_snappelPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            vh.txt_snappelPrice.setTextColor(Color.RED);
                            vh.txt_sellingPrice.setText(priceDataList.get(j).getSelling_price());

                            if (priceDataList != null && priceDataList.size() > 0) {
                                for (int k = 0; k < priceDataList.size(); k++) {
                                    if (vh.sp_quantityName.getSelectedItem().toString().trim().equalsIgnoreCase(priceDataList.get(k).getQuantity_name().trim())) {
                                        if (Integer.parseInt(priceDataList.get(k).getTotal_availability()) > 0) {
                                            vh.btn_add.setText("Add");
                                            vh.btn_add.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
                                            vh.btn_add.setTextColor(context.getResources().getColor(R.color.white));
                                            DataSource dataSource = new DataSource(context);
                                            dataSource.open();
                                            ProductData objproductData = null;
                                            try {
                                                objproductData = dataSource.selectInto_tbl_Products(productData.get(i).getId(), vh.sp_quantityName.getSelectedItem().toString()).get(0);
                                            } catch (Exception e) {
                                                objproductData = null;
                                            }
                                            if (objproductData != null) {
                                                vh.btn_add.setVisibility(View.GONE);
                                                vh.ll_carditem.setVisibility(View.VISIBLE);
                                                vh.txt_prno.setText(objproductData.getSelected_quantity());
                                            } else {
                                                vh.btn_add.setVisibility(View.VISIBLE);
                                                vh.ll_carditem.setVisibility(View.GONE);
                                            }
                                            dataSource.close();
                                            break;
                                        } else {
                                            vh.btn_add.setText("Out of stock");
                                            vh.btn_add.setTextColor(context.getResources().getColor(R.color.white));
                                            vh.btn_add.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.redone));
                                        }
                                    }
                                }
                            }
                        }
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductData obj_productDataobj = new ProductData();
                    obj_productDataobj.setId(productData.get(i).getId());
                    obj_productDataobj.setProductname(productData.get(i).getProductname());
                    obj_productDataobj.setGst(productData.get(i).getGst());
                    obj_productDataobj.setSku(productData.get(i).getSku());
                    obj_productDataobj.setDescription(productData.get(i).getDescription());
                    obj_productDataobj.setType_name_id(productData.get(i).getType_name_id());
                    obj_productDataobj.setType_name(productData.get(i).getType_name());
                    obj_productDataobj.setProduct_about(productData.get(i).getProduct_about());
                    obj_productDataobj.setProduct_ingredient(productData.get(i).getProduct_ingredient());
                    obj_productDataobj.setProductinfo(productData.get(i).getProductinfo());
                    obj_productDataobj.setStatus(productData.get(i).getStatus());
                    obj_productDataobj.setCreated_by(productData.get(i).getCreated_by());
                    obj_productDataobj.setShop_id(productData.get(i).getShop_id());
                    obj_productDataobj.setBrand_id(productData.get(i).getBrand_id());
                    obj_productDataobj.setBrandname(productData.get(i).getBrandname());
                    obj_productDataobj.setBrand_image(productData.get(i).getBrand_image());
                    obj_productDataobj.setProduct_image(productData.get(i).getProduct_image());
                    /*For price*/
                    if (priceDataList != null && priceDataList.size() > 0) {
                        for (int j = 0; j < priceDataList.size(); j++) {
                            if (vh.sp_quantityName.getSelectedItem().toString().trim().equalsIgnoreCase(priceDataList.get(j).getQuantity_name().trim())) {
                                obj_productDataobj.setCity_id(priceDataList.get(j).getCity_id());
                                obj_productDataobj.setQuantity_id(priceDataList.get(j).getQuantity_id());
                                obj_productDataobj.setPrice(priceDataList.get(j).getPrice());
                                obj_productDataobj.setSelling_price(priceDataList.get(j).getSelling_price());
                                obj_productDataobj.setDiscount(priceDataList.get(j).getDiscount());
                                obj_productDataobj.setTotal_availability(priceDataList.get(j).getTotal_availability());
                                obj_productDataobj.setQuantity_name(priceDataList.get(j).getQuantity_name());
                                obj_productDataobj.setSelected_quantity("1");
                                obj_productDataobj.setCity_name(priceDataList.get(j).getCity_name());
                                abc=priceDataList.get(j).getQuantity_name();
                                break;
                            }
                        }
                    }
                    Intent intent = new Intent(context, ProductDetails.class);
                    intent.putExtra("ProductDetails", (Serializable) obj_productDataobj);
                    intent.putExtra("fromWhere", productData.get(i).getParent_category_name());
                    intent.putExtra("product_id", productData.get(i).getId());
                    intent.putExtra("product_quantity",abc);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return productData.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView txt_brandName, txt_typeName, txt_snappelPrice, txt_sellingPrice, txt_discount, txt_prno;
        Spinner sp_quantityName;
        ImageView img_product;
        TextView txt_minus, txt_plus;
        Button btn_add;
        LinearLayout ll_carditem;

        public VH(@NonNull View itemView) {
            super(itemView);



            txt_brandName = itemView.findViewById(R.id.txt_brandName);
            txt_typeName = itemView.findViewById(R.id.txt_typeName);
            txt_snappelPrice = itemView.findViewById(R.id.txt_snappelPrice);
            txt_sellingPrice = itemView.findViewById(R.id.txt_sellingPrice);
            txt_discount = itemView.findViewById(R.id.txt_discount);
            sp_quantityName = itemView.findViewById(R.id.sp_quantityName);
            img_product = itemView.findViewById(R.id.img_product);
            txt_minus = itemView.findViewById(R.id.txt_minus);
            txt_plus = itemView.findViewById(R.id.txt_plus);
            txt_prno = itemView.findViewById(R.id.txt_prno);
            btn_add = itemView.findViewById(R.id.btn_add);
            ll_carditem = itemView.findViewById(R.id.ll_carditem);
        }
    }
}
