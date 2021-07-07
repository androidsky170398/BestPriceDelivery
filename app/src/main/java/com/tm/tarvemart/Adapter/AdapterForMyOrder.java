package com.tm.tarvemart.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.tm.tarvemart.Util.Constants;
import com.tm.tarvemart.entity.ProductData;
import com.tm.tarvemart.mainUI.MyOrderActivity;
import com.tm.tarvemart.mainUI.MyOrderDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tm.tarvemart.Util.Constants.ID;
import static com.tm.tarvemart.inerface.GlobleInterfce.Base_URl;

public class AdapterForMyOrder extends RecyclerView.Adapter<AdapterForMyOrder.ViewHolder> {
    Context context;
    List<ProductData> productDataList = new ArrayList<>();

    public AdapterForMyOrder(Context context, List<ProductData> productDataList) {
        this.context = context;
        this.productDataList = productDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_for_myorder, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.tv_inviceId.setText(productDataList.get(i).getInvoice_id());
        viewHolder.tv_orderDate.setText(productDataList.get(i).getOrder_date());

      if (productDataList.get(i).getStatus().equalsIgnoreCase("cancel")){
          viewHolder.tv_Status.setText(productDataList.get(i).getStatus());
          viewHolder.tv_Status.setTextColor(Color.RED);
      }
      else {
          viewHolder.tv_Status.setText(productDataList.get(i).getStatus());
          viewHolder.tv_Status.setTextColor(Color.GREEN);
      }

        if (productDataList.get(i).getStatus() != null && productDataList.get(i).getStatus().equalsIgnoreCase("complete")) {
            viewHolder.can_ll.setVisibility(View.INVISIBLE);
        }

        if (productDataList.get(i).getStatus() != null && productDataList.get(i).getStatus().equalsIgnoreCase("pending")) {
            viewHolder.tv_Status.setTextColor(Color.BLUE);
        }

        if (productDataList.get(i).getStatus() != null && productDataList.get(i).getStatus().equalsIgnoreCase("cancel")) {
            viewHolder.tv_cancelOrder.setVisibility(View.GONE);
        } else {
            viewHolder.tv_cancelOrder.setVisibility(View.VISIBLE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnectionHelper.isOnline(context)) {
                    context.startActivity(new Intent(context, MyOrderDetails.class)
                            .putExtra("order_id", productDataList.get(i)
                                    .getId()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    Toast.makeText(context, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }

            }
        });
        viewHolder.tv_cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure want to cancel your order?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (NetworkConnectionHelper.isOnline(context)) {
                            final ProgressDialog progressDialog = new ProgressDialog(context);
                            progressDialog.setMessage("Loading");
                            progressDialog.show();
                            String url = "delete-order";
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    //Toast.makeText(AddressList.this, response, Toast.LENGTH_SHORT).show();
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getBoolean("status")) {
                                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(context, MyOrderActivity.class);
                                            context.startActivity(intent);
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
                                    params.put("customer_id", Constants.getSavedPreferences(context, ID, ""));
                                    params.put("order_id", productDataList.get(i).getId());
                                    params.put("cancel_reason", "N/A");
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
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_inviceId, tv_orderDate, tv_Status, tv_cancelOrder;
        LinearLayout can_ll;

        public ViewHolder(View v) {
            super(v);
            tv_inviceId = v.findViewById(R.id.tv_inviceId);
            tv_orderDate = v.findViewById(R.id.tv_orderDate);
            tv_Status = v.findViewById(R.id.tv_Status);
            tv_cancelOrder = v.findViewById(R.id.tv_cancelOrder);
            can_ll=v.findViewById(R.id.can_ll);

        }
    }
}
