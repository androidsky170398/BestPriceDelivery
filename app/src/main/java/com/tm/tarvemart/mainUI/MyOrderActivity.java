package com.tm.tarvemart.mainUI;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tm.tarvemart.Adapter.AdapterForMyOrder;
import com.tm.tarvemart.Helper.NetworkConnectionHelper;
import com.tm.tarvemart.R;
import com.tm.tarvemart.Util.Constants;
import com.tm.tarvemart.entity.ProductData;
import com.tm.tarvemart.inerface.GlobleInterfce;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.tm.tarvemart.Util.Constants.ID;

public class MyOrderActivity extends AppCompatActivity implements GlobleInterfce {
    RecyclerView rv_myOrder;
    TextView tv_noData;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        findViewByIds();
        getSupportActionBar().setTitle("My Orders");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gridLayoutManager = new GridLayoutManager(MyOrderActivity.this, 1);
        rv_myOrder.setHasFixedSize(true);
        rv_myOrder.setLayoutManager(gridLayoutManager);
        if (NetworkConnectionHelper.isOnline(MyOrderActivity.this)) {
            final ProgressDialog progressDialog = new ProgressDialog(MyOrderActivity.this);
            progressDialog.setMessage("Loading");
            progressDialog.show();
            String url = "order-list";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    //Toast.makeText(AddressList.this, response, Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("status")) {
                            List<ProductData> dataList = ProductData.createJsonInList(jsonObject.getString("data"));
                            if (dataList != null && dataList.size() > 0) {
                                AdapterForMyOrder adapterForMyOrder = new AdapterForMyOrder(MyOrderActivity.this, dataList);
                                rv_myOrder.setAdapter(adapterForMyOrder);
                                rv_myOrder.setVisibility(View.VISIBLE);
                                tv_noData.setVisibility(View.GONE);
                            }
                        } else {
                            rv_myOrder.setVisibility(View.GONE);
                            tv_noData.setVisibility(View.VISIBLE);
                            Toast.makeText(MyOrderActivity.this, "No Order Found!!!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(MyOrderActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("customer_id", Constants.getSavedPreferences(MyOrderActivity.this, ID, ""));
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(MyOrderActivity.this);
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(MyOrderActivity.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
        }
        //Dashboard.checkNetConnection(MyOrderActivity.this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //Dashboard.checkNetConnection(MyOrderActivity.this);
    }

    private void findViewByIds() {
        rv_myOrder = findViewById(R.id.rv_myOrder);
        tv_noData = findViewById(R.id.tv_noData);
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
