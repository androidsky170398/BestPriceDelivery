package com.tm.tarvemart.mainUI;

import android.app.ProgressDialog;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tm.tarvemart.Adapter.AdapterForMyOrderProducts;
import com.tm.tarvemart.Helper.NetworkConnectionHelper;
import com.tm.tarvemart.R;
import com.tm.tarvemart.entity.ProductData;
import com.tm.tarvemart.inerface.GlobleInterfce;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyOrderDetails extends AppCompatActivity implements GlobleInterfce {

    TextView tv_inviceId, tv_orderDate, tv_Status, tv_ActualPrice, tv_TotalPrice, tv_deliveryType, tv_DeliveryDate, tv_DeliveryAddress, tv_PaymentMode;
    String order_id;
    RecyclerView rv_product;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_details);
        getSupportActionBar().setTitle("Order Details");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewByIds();
        gridLayoutManager = new GridLayoutManager(MyOrderDetails.this, 1);
        rv_product.setHasFixedSize(true);
        rv_product.setLayoutManager(gridLayoutManager);
        order_id = getIntent().getStringExtra("order_id");

        if (NetworkConnectionHelper.isOnline(MyOrderDetails.this)) {
            getOrderDetails();
        } else {
            Toast.makeText(MyOrderDetails.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
        }
        //Dashboard.checkNetConnection(MyOrderDetails.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Dashboard.checkNetConnection(MyOrderDetails.this);
    }

    private void findViewByIds() {
        tv_inviceId = findViewById(R.id.tv_inviceId);
        tv_orderDate = findViewById(R.id.tv_orderDate);
        tv_Status = findViewById(R.id.tv_Status);
        tv_ActualPrice = findViewById(R.id.tv_ActualPrice);
        tv_TotalPrice = findViewById(R.id.tv_TotalPrice);
        tv_deliveryType = findViewById(R.id.tv_deliveryType);
        tv_DeliveryDate = findViewById(R.id.tv_DeliveryDate);
        tv_DeliveryAddress = findViewById(R.id.tv_DeliveryAddress);
        rv_product = (RecyclerView) findViewById(R.id.rv_product);
        tv_PaymentMode = findViewById(R.id.tv_PaymentMode);
    }

    private void getOrderDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(MyOrderDetails.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        String url = "order-details";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                //Toast.makeText(AddressList.this, response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        ProductData productData = ProductData.createJsonInObject(jsonObject.getString("data"));
                        if (productData != null) {
                            tv_inviceId.setText(productData.getInvoice_id());
                            tv_orderDate.setText(productData.getOrder_date());
                            tv_Status.setText(productData.getStatus());
                            tv_ActualPrice.setText("Rs. " + productData.getActual_price());
                            tv_TotalPrice.setText("Rs. " + productData.getTotal_price());
                            tv_deliveryType.setText(productData.getDelivery_type());
                            tv_DeliveryDate.setText(productData.getDelivery_date());
                            tv_PaymentMode.setText(productData.getPayment_mode());
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                tv_DeliveryAddress.setText(Html.fromHtml(productData.getDelivery_address(), Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                tv_DeliveryAddress.setText(Html.fromHtml(productData.getDelivery_address()));
                            }
                            if (productData.getOrders() != null && productData.getOrders().size() > 0) {
                                AdapterForMyOrderProducts adapterForMyOrderProducts = new AdapterForMyOrderProducts(MyOrderDetails.this, productData.getOrders());
                                rv_product.setAdapter(adapterForMyOrderProducts);
                            }
                        }
                    } else {

                        Toast.makeText(MyOrderDetails.this, "Some error occurred!!!", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MyOrderDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("order_id", order_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MyOrderDetails.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
