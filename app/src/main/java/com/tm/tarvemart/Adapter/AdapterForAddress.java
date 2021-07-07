package com.tm.tarvemart.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tm.tarvemart.Helper.NetworkConnectionHelper;
import com.tm.tarvemart.R;
import com.tm.tarvemart.entity.AddressData;
import com.tm.tarvemart.inerface.GlobleInterfce;
import com.tm.tarvemart.mainUI.AddAndEditAddress;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterForAddress extends RecyclerView.Adapter<AdapterForAddress.VH> implements GlobleInterfce {
    Context context;
    List<AddressData> addressDataList = new ArrayList<>();
    String totalPrice;

    public AdapterForAddress(Context context, List<AddressData> addressDataList, String totalPrice) {
        this.context = context;
        this.addressDataList = addressDataList;
        this.totalPrice = totalPrice;
    }

    @NonNull
    @Override
    public AdapterForAddress.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.address_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForAddress.VH vh, final int i) {
        vh.tv_address_type.setText(addressDataList.get(i).getType());
        vh.txt_fullname.setText(addressDataList.get(i).getFirst_name() + " " + addressDataList.get(i).getLast_name());
        vh.txt_houseNo.setText(addressDataList.get(i).getHouse_number());
        vh.txt_apartname.setText(addressDataList.get(i).getApartment());
        vh.txt_street.setText(addressDataList.get(i).getStreet_detail());
        vh.txt_landmark.setText(addressDataList.get(i).getLandmark());
        vh.txt_city.setText(addressDataList.get(i).getCity_name());
        vh.txt_pincode.setText(addressDataList.get(i).getPincode());
        vh.txt_contact.setText(addressDataList.get(i).getMobile());
        vh.txt_deliveryhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        vh.img_deletaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnectionHelper.isOnline(context)) {
                    final ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Loading");
                    progressDialog.show();
                    String url = "delete-address";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            //Toast.makeText(AddressList.this, response, Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("status")) {
                                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    context.startActivity(new Intent(context, context.getClass()).putExtra("totalPrice", totalPrice));
                                    ((Activity) context).finish();
                                } else {
                                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("address_id", addressDataList.get(i).getId());
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest);
                } else {
                    Toast.makeText(context, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }

            }
        });
        vh.img_editaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnectionHelper.isOnline(context)) {
                    context.startActivity(new Intent(context, AddAndEditAddress.class).putExtra("totalPrice", totalPrice)
                            .putExtra("addressData", (Serializable) addressDataList.get(i)));
                    ((Activity) context).finish();
                    //AddressList.alertAdd_And_DeleteAddress(context, addressDataList.get(i), totalPrice);
                } else {
                    Toast.makeText(context, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return addressDataList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView txt_fullname, txt_houseNo, txt_apartname,
                txt_street,
                txt_landmark,
                txt_city,
                txt_pincode,
                txt_contact,
                txt_deliveryhere,
                tv_address_type;
        ImageView img_deletaddress, img_editaddress;

        public VH(@NonNull View itemView) {
            super(itemView);
            txt_fullname = itemView.findViewById(R.id.txt_fullname);
            txt_houseNo = itemView.findViewById(R.id.txt_houseNo);
            txt_apartname = itemView.findViewById(R.id.txt_apartname);
            txt_street = itemView.findViewById(R.id.txt_street);
            txt_landmark = itemView.findViewById(R.id.txt_landmark);
            txt_city = itemView.findViewById(R.id.txt_city);
            txt_pincode = itemView.findViewById(R.id.txt_pincode);
            txt_contact = itemView.findViewById(R.id.txt_contact);
            txt_deliveryhere = itemView.findViewById(R.id.txt_deliveryhere);
            tv_address_type = itemView.findViewById(R.id.tv_address_type);
            img_deletaddress = itemView.findViewById(R.id.img_deletaddress);
            img_editaddress = itemView.findViewById(R.id.img_editaddress);
        }
    }
}
